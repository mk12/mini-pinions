;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.game
  "Implements the game world, where all the fun happens."
  (:require [quil.core :as q]
            [mini-pinions.button :as b]
            [mini-pinions.common :as c]
            [mini-pinions.level :as l]
            [mini-pinions.menu :as m]
            [mini-pinions.planet :as p]
            [mini-pinions.curve :as u]
            [mini-pinions.vector :as v])
  (:import java.lang.Math))

;;;;; Constants

(def gravity-fly 0.2)
(def gravity-fall 0.9)
(def min-x-speed 1.2)
(def slide-threshold 0.7)
(def elasticity 0.3)
(def path-friction 0.987)

(def space-gravity-coef 0.5)
(def transition-height (* 1.9 c/height))
(def space-height (* 2.5 c/height))
(def max-height (* 4 c/height))

(def fledge-radius 9)
(def curve-resolution 40)
(def top-margin 10)

(def sky-color [160 240 255])
(def space-color (map #(* 0.25 %) sky-color))
(def path-color [130 210 0])
(def fledge-color [210 110 0])

(def paused-message "Click To Play")
(def message-size 40)
(def message-color [0 0 50])
(def message-pos [c/half-width (/ c/half-height 2)])

(def score-text-size 25)
(def score-pos (v/make c/half-width (+ score-text-size 5)))
(def score-multiplier-text-size 20)
(def score-multiplier-pos (v/make c/half-width 30))

;;;;; Buttons

(defn menu-action [world]
  (c/init {:name :menu, :game (assoc world :paused true)}))

(def buttons
  [(assoc m/menu-button :action menu-action)
   (b/make-control
     "II"
     #(assoc % :paused (not (:paused %)))
     b/top-right-2
     [100 100 100])
   (b/make-control
     "R"
     #(c/init {:name :game :level (:level %)})
     b/top-right-1
     [200 200 200])])

;;;;; Physics

(defn fledge-pos
  "Gets the position component of the Fledge value."
  [[pos _]]
  pos)

(defn reflect
  "Reflects vector v across normal vector n."
  [v n]
  (-> (v/dot v n)
      (* 2)
      (/ (v/norm-sq n))
      (v/scale n)
      (v/negate)
      (v/add v)))

(defn collide-path-fledge
  "Calculate a new velocity for Fledge after colliding with the path at a point
  where it has height y and slope m."
  [vel0 y m]
  (let [tangent (v/normalize (v/make 1 m))
        speed (v/norm vel0)
        dot (v/dot (v/div speed vel0) tangent)]
    (if (> dot slide-threshold)
      (v/scale (* speed path-friction) tangent)
      (v/scale elasticity (reflect vel0 (v/make (- m) 1))))))

(defn keep-going
  "Ensures that the x-component of the velocity is above a certain value."
  [x [pos0 vel0]]
  [pos0 (if (< (v/x vel0) x)
          (v/make x (v/y vel0))
          vel0)])

(defn update-fledge-path
  "Calculates a new position and velocity for Fledge given the old ones as well
  as the magnitude of the acceleration due to gravity and the path."
  [[pos0 vel0] gravity path]
  (let [accel (v/make 0 (- gravity))
        vel (v/add vel0 accel)
        pos (v/add pos0 vel)
        path-y (u/path-val :y path (v/x pos))
        min-y (+ path-y fledge-radius)]
    (keep-going
      min-x-speed
      (if (>= (v/y pos) min-y)
        [pos vel]
        [(v/make (v/x pos) min-y)
         (collide-path-fledge vel path-y (u/path-val :m path (v/x pos)))]))))

(defn transition-pos
  "Calculate the position in the transition to outer space, clamped to [0,1]."
  [pos]
  (c/clamp (c/range->normal (v/y pos) transition-height space-height)))

(defn limit-height
  "Prevent Fledge's height from exceeding a certain value."
  [y [pos0 vel0]]
  [(if (> (v/y pos0) y)
     (v/make (v/x pos0) y)
     pos0)
   vel0])

(defn update-fledge-space
  "Calculates a new position and velocity for Fledge like update-fledge-path,
  except the calcuations are for outer space."
  [[pos0 vel0] gravity galaxy]
  (let [coef (c/normal->range (transition-pos pos0) 1 space-gravity-coef)
        accel-down (v/make 0 (* -1 coef gravity))
        accel (v/add accel-down (p/net-attraction pos0 galaxy))
        vel (v/add vel0 accel)
        pos (v/add pos0 vel)
        planet (p/colliding-planet pos fledge-radius galaxy)]
    (with-meta
      (keep-going
        min-x-speed
        (limit-height
          max-height
          (if planet
            [(p/stop-collision pos0 fledge-radius planet) v/zero]
            [pos vel])))
      (if planet {:collide true}))))

;;;;; Draw

(defn draw-message
  "Draws a text message prominently."
  [message]
  (c/fill-color message-color)
  (q/text-size message-size)
  (c/draw-text message message-pos))

(defn draw-score
  "Draws the score in the corner of the screen."
  [world]
  (let [transition (transition-pos (fledge-pos (:fledge world)))]
    (c/fill-grey (* 255 transition))
    (q/text-size score-text-size)
    (c/draw-text (str (int (:score world))) score-pos)
    (when (> transition 0)
      (q/text-size score-multiplier-text-size)
      (c/draw-text (str "x" (int (* transition 10))) score-multiplier-pos))))

(defn lerp-color
  "Linearly interpolates between two colours."
  [t c1 c2]
  (map (partial c/normal->range t) c1 c2))

(defn calc-color
  "Calculates the background color to use based on position."
  [pos]
  (lerp-color (transition-pos pos) sky-color space-color))

;;;;; Transform

(defn calc-path-bounds
  "Calculates the interval over which to draw, given the position of the center
  of the screen, the scaling factor, and the resolution of the curve. The left
  and right bounds of the interval lie on multiples of the resolution so that
  the path shape will look consistent and memoization will be effective."
  [center scale res]
  (let [half-view-width (/ c/half-width scale)
        left (- (v/x center) half-view-width)
        right (+ (v/x center) half-view-width)]
    [(* (Math/floor (/ left res)) res)
     (* (Math/ceil (/ right res)) res)]))

(defn calc-galaxy-bounds
  "Calculates the interval over which to draw the planets in the galaxy."
  [center]
  [(- (v/x center) c/half-width)
   (+ (v/x center) c/half-width)])

(defn calc-scale
  "Calculates the scale factor to draw with based on position."
  [pos]
  (let [y (+ (v/y pos) fledge-radius top-margin)
        th transition-height]
    (c/clamp
      (if (< y th)
        (/ c/height y)
        (c/range->range (v/y pos) th space-height (/ c/height th) 1)))))

(defn calc-translate
  "Calculates the translation to draw with based on position and scale."
  [pos scale]
  (-> (- 1 (transition-pos pos))
      c/square
      (* (- c/half-height fledge-radius top-margin))
      (+ c/half-height)
      (/ scale)
      (- (v/y pos))
      (min 0)))

(defn transform
  "Applies all necessary transformations for the given position. Also transforms
  the origin to the bottom-left instead of the top-left."
  [pos]
  (let [scale (calc-scale pos)]
    (q/translate 0 c/height)
    (q/scale 1 -1)
    (q/translate c/half-width 0)
    (q/scale scale scale)
    (q/translate (- (v/x pos))
                 (calc-translate pos scale))))

;;;;; Score

(defn score-earned
  "Calculates the value to be added to the current score."
  [fledge]
  (let [pos (fledge-pos fledge)
        collide (:collide (meta fledge))
        transition (transition-pos pos)]
    (max 0
         (+ (* 0.001 (v/y pos))
            (* 0.01 transition (v/y pos))
            (if collide -2000 0)))))

;;;;; World

(defmethod c/init :game [world]
  (let [level-data (l/level-n (:level world))]
    (assoc world
           :level-data level-data
           :paused true
           :falling false
           :score 0
           :fledge [(:start level-data) v/zero])))

(defn new-fledge
  "Returns the new Fledge for the next state of the world (helper function)."
  [world]
  (let [fledge0 (:fledge world)
        [pos _] fledge0
        level-data (:level-data world)
        below-space (= (transition-pos pos) 0)
        gravity (if (q/mouse-state) gravity-fall gravity-fly)]
    (if below-space
      (update-fledge-path fledge0 gravity (:path level-data))
      (update-fledge-space
        fledge0
        gravity
        (if-not (:falling world)
          (p/subgalaxy (calc-galaxy-bounds pos) (:galaxy level-data)))))))

(defmethod c/update :game [world]
  (if (:paused world)
    world
    (let [fledge (new-fledge world)]
      (assoc world
             :fledge fledge
             :score (+ (:score world) (score-earned fledge))
             :falling (and (> (transition-pos (fledge-pos fledge)) 0)
                           (or (:falling world) (:collide (meta fledge))))))))

(defmethod c/input :game [world]
  (or (b/button-action buttons world)
      (if (and (:paused world) (q/mouse-state))
        (assoc world :paused false))
      world))

(defmethod c/draw :game [world]
  (let [[pos _] (:fledge world)
        scale (calc-scale pos)
        res (/ curve-resolution scale)
        bounds (calc-path-bounds pos scale res)
        galaxy-bounds (calc-galaxy-bounds pos)
        transition (transition-pos pos)
        level-data (:level-data world)]
    (c/clear-background (calc-color pos))
    (c/restore-matrix
      (transform pos)
      (c/fill-color fledge-color)
      (c/draw-circle pos fledge-radius)
      (c/fill-color path-color)
      (if (= transition 0)
        (u/draw-path (:path level-data) bounds res)
        (p/draw-galaxy (:galaxy level-data) bounds (* 255 transition))))
    (if (:paused world) (draw-message paused-message))
    (b/draw-buttons buttons)
    (draw-score world)))
