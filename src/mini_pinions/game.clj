;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.game
  "Implements the game World, where all the fun happens."
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.curve :as u]
            [mini-pinions.vector :as v]))

;;;;; Constants

(def radius 10)
(def g-fly 1)
(def g-fall 3)

(def curve-resolution 10)

;;;;; Levels

(def levels
  [{:start (v/make 100 300)
    :path
    (u/make-path
      v/zero
      [{:height 300
        :direction :up
        :half-cycles 1
        :width 200}
      {:height 250
        :direction :down
        :half-cycles 1
        :width 150}
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
      {:height 25
        :direction :down
        :half-cycles 15
        :width 105}])}])

;;;;; World

(defmethod c/init :game [world]
  (let [level-data (nth levels (- (:level world) 1))]
    (assoc world
           :level-data level-data
           :pos (:start level-data)
           :vel (v/make 0 0))))

(defmethod c/update :game [world]
  (let [accel (if (q/mouse-state) g-fall g-fly)
        new-vel (v/make 5 0)];(v/add (v/make 0 accel) (:vel world))]
    (assoc world
           :vel new-vel
           :pos (v/add new-vel (:pos world)))))

(defmethod c/draw :game [world]
  (let [pos (:pos world)
        left (- (v/x pos) (/ c/width 2) 50)
        right (+ left c/width 50)]
    (q/background 100)
    (q/scale 1 -1)
    (q/translate 0 (- c/height))
    (q/translate (- (/ c/width 2) (v/x pos)) 0)
    (u/draw-path (:path (:level-data world)) left right curve-resolution)
    (q/ellipse (v/x pos) (v/y pos) radius radius)))
