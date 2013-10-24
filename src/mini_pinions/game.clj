;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.game
  "Implements the game World, where all the fun happens."
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.curve :as u]
            [mini-pinions.vector :as v])
  (:import java.lang.Math))

;;;;; Constants

(def fledge-radius 3)
(def g-fly 0.2)
(def g-fall 0.9)
(def elasticity 0.6)
(def path-friction 0.99)
(def min-x-speed 1.3)
(def rebound-threshold 0.7)

(def curve-resolution 20)

;;;;; Levels

(def levels
  [{:start (v/make 402 (+ 500 fledge-radius))
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
       {:height 50
        :direction :down
        :half-cycles 1200
        :width 100000}])}])

;;;;; Physics

(defn dist-derivative-y
  "This isn't used yet."
  [pos x]
  (+ (Math/sin (* 2 x))
     (* -2 (v/y pos) (Math/cos x))
     (* -2 (v/x pos))))

;;;;; Draw

(defn path-bounds
  "Calculates the interval over which to draw given the position of the center
  of the screen, the scaling factor, and the resolution of the curve. The left
  and right bounds of the interval lie on multiples of the resolution so that
  the path shape will look consistent and memoization will be effective."
  [center scale resolution]
  (let [half-view-width (/ c/half-width scale)
        left (- (v/x center) half-view-width)
        right (+ (v/x center) half-view-width)]
    [(* (Math/floor (/ left resolution)) resolution)
     (* (Math/ceil (/ right resolution)) resolution)]))

; TODO: maximum (game freezes when trying to render too much b/c of tiny scale)
(defn path-scale
  "Calculates the scale factor that should be used based on position."
  [center]
  (let [sky (- (v/y center) c/height)]
    (if (pos? sky)
      (- 1 (* sky 0.0001))
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

;;;;; World

(defmethod c/init :game [world]
  (let [level-data (nth levels (- (:level world) 1))]
    (assoc world
           :line [[0 0] [0 0]]
           :level-data level-data
           :pos (:start level-data)
           :vel (v/make 5 0))))

(defn keep-going
  "Ensures that the x-component of a vector is above a certain value."
  [v x]
  (if (< (v/x v) x)
    (v/make x (v/y v))
    v))

;;; I know this is too complex. I will refactor it when I have time.
(defmethod c/update :game [world]
  (let [accel (v/make 0 (- (if (q/mouse-state) g-fall g-fly)))
        new-vel (v/add (:vel world) accel)
        new-pos (v/add (:pos world) new-vel)
        path-y (u/path-y (:path (:level-data world)) (v/x new-pos) u/curve-y)
        min-y (+ path-y fledge-radius)]
    (if (< (v/y new-pos) min-y)
      (let [tangent-m (u/path-y (:path (:level-data world)) (v/x new-pos) u/curve-slope)
            tangent (v/normalize (v/make 1 tangent-m))
            normal-m (- (/ tangent-m))
            normal (v/normalize (if normal-m (v/make 1 normal-m) (v/make 0 1)))
            dot (v/dot (v/normalize new-vel) normal)
            scaled-normal (v/scale (* 2 dot) normal)
            reflected-dir (v/normalize (v/sub (v/normalize new-vel) scaled-normal))
            reflected-vel (v/scale (v/norm new-vel) reflected-dir)
            new-new-pos (v/make (v/x new-pos) min-y)]
        (if (< (Math/abs dot) rebound-threshold)
          (assoc world
                 :pos new-new-pos
                 :vel (keep-going (v/scale (* (v/norm new-vel) path-friction) tangent) min-x-speed))
            (assoc world :pos new-new-pos :vel (keep-going (v/scale elasticity reflected-vel) min-x-speed))))
      (assoc world :pos new-pos :vel new-vel))))

;Ray x' $ normalize $ v <-> 2 * v <.> n *> n


(defmethod c/draw :game [world]
  (let [pos (:pos world)
        scale (path-scale pos)
        bounds (path-bounds pos scale curve-resolution)]
    (q/background 100)
    (transform pos scale)
    (u/draw-path (:path (:level-data world)) bounds curve-resolution)
    (c/draw-ellipse pos fledge-radius)
    (c/draw-line (nth (:line world) 0) (nth (:line world) 1))))
