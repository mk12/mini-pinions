;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.game
  "Implements the game world, where all the fun happens."
  (:require [quil.core :as q]
            [mini-pinions.button :as b]
            [mini-pinions.common :as c]
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

(def fledge-radius 5)
(def curve-resolution 8)
(def top-margin 10)

(def background-color [160 240 255])
(def path-color [130 210 0])
(def fledge-color [210 110 0])

(def frozen-message "Click To Start")
(def message-size 40)
(def message-color [0 0 50])
(def message-pos [c/half-width (/ c/half-height 2)])

;;;;; Levels

(def levels
  [{:start (v/make 400 (+ 300 fledge-radius))
    :path
    (u/make-path
      v/zero
      [{:height 300
        :direction :up
        :half-cycles 1
        :width 400}
       {:height 250
        :direction :down
        :half-cycles 1
        :width 300}
       {:height 175
        :direction :up
        :half-cycles 3
        :width 400}
       {:height 25
        :direction :down
        :half-cycles 20
        :width 600}
       {:height 100
        :direction :down
        :half-cycles 1
        :width 70}
       {:height 200
        :direction :up
        :half-cycles 600
        :width 150000}])}])

;;;;; Physics

; TODO: finish this
(defn dist-derivative-y
  "This isn't used yet."
  [pos x]
  (+ (Math/sin (* 2 x))
     (* -2 (v/y pos) (Math/cos x))
     (* -2 (v/x pos))))

(defn reflect
  "Reflects vector v across normal vector n."
  [v n]
  (-> (v/dot v n)
      (* 2)
      (/ (v/norm-sq n))
      (v/scale n)
      (v/negate)
      (v/add v)))

(defn collide-fledge
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
  "Ensures that the x-component of a velocity is above a certain value."
  [x [pos vel]]
  [pos (if (< (v/x vel) x)
         (v/make x (v/y vel))
         vel)])

(defn update-fledge
  "Calculates a new position and velocity for Fledge given the old ones as well
  as the magnitude of the acceleration due to gravity and the path."
  [pos0 vel0 gravity path]
  (let [accel (v/make 0 (- gravity))
        vel (v/add vel0 accel)
        pos (v/add pos0 vel)
        path-y (u/path-val :y path (v/x pos))
        min-y (+ path-y fledge-radius)]
    (keep-going
      min-x-speed
      (if (< (v/y pos) min-y)
        [(v/make (v/x pos) min-y)
        (collide-fledge vel path-y (u/path-val :m path (v/x pos)))]
        [pos vel]))))

;;;;; Draw

(defn path-bounds
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

(defn path-scale
  "Calculates the scale factor that should be used based on position."
  [center]
  (let [top-y (+ (v/y center) top-margin)]
    (if (> top-y c/height)
      (/ c/height top-y)
      1)))

(defn transform
  "Applies all necessary transformations to center the view around the given
  point with the given scale. Also transforms the origin to the top-left so that
  Quil draw calls will work properly."
  [center scale]
  (q/translate 0 c/height)
  (q/scale 1 -1)
  (q/translate c/half-width 0)
  (q/scale scale scale)
  (q/translate (- (v/x center)) 0))

(defn draw-message
  "Draws a text message prominently."
  [message]
  (apply q/fill message-color)
  (q/text-size message-size)
  (c/draw-text message message-pos))

;;;;; Buttons

(def menu-button
  (b/make-button "<" :menu (v/make 20 20) (v/make 30 30) [255 255 255]))

;;;;; World

(defmethod c/init :game [world]
  (let [level-data (nth levels (- (:level world) 1))]
    (assoc world
           :level-data level-data
           :frozen true
           :pos (:start level-data)
           :vel v/zero)))

(defmethod c/update :game [world]
  (if (:frozen world)
    world
    (let [gravity (if (q/mouse-state) gravity-fall gravity-fly)
          path (:path (:level-data world))
          [pos vel] (update-fledge (:pos world) (:vel world) gravity path)]
      (assoc world :pos pos :vel vel))))

(defmethod c/input :game [world]
  (if (q/mouse-state)
    (cond
      (b/mouse-in-button? menu-button) (c/make-world (:action menu-button))
      (:frozen world) (assoc world :frozen false)
      :else world)
    world))

(defmethod c/draw :game [world]
  (let [pos (:pos world)
        scale (path-scale pos)
        res (/ curve-resolution scale)
        bounds (path-bounds pos scale res)]
    (apply q/background background-color)
    (if (:frozen world) (draw-message frozen-message))
    (b/draw-button menu-button)
    (transform pos scale)
    (apply q/fill path-color)
    (u/draw-path (:path (:level-data world)) bounds res)
    (apply q/fill fledge-color)
    (c/draw-ellipse pos fledge-radius)))
