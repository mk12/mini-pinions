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

;;;;; Levels

(def level-1
  (u/make-path
    v/zero
    [{:height 30
      :direction :up
      :half-cycles 1
      :width 100}
     {:height 25
      :direction :down
      :half-cycles 1
      :width 50}
     {:height 200
      :direction :up
      :half-cycles 1
      :width 75}
     {:height 25
      :direction :down
      :half-cycles 20
      :width 400}
     {:height 100
      :direction :down
      :half-cycles 1
      :width 70}
     {:height 25
      :direction :down
      :half-cycles 15
      :width 105}]))

;;;;; World

(defmethod c/init :game [world]
  (assoc world
         :pos (v/make 0 0)
         :vel (v/make 0 0)))

(defmethod c/update :game [world]
  (let [accel (if (q/mouse-state) g-fall g-fly)
        new-vel (v/add (v/make 0 accel) (:vel world))]
    (assoc world
           :vel new-vel
           :pos (v/add new-vel (:pos world)))))

(defmethod c/draw :game [world]
  (let [pos (:pos world)]
    (q/scale 1 -1)
    (q/translate 0 (- c/height))
    (q/background 100)
    (u/draw-path level-1 0 c/width 20)
    (q/ellipse c/center-x (v/y pos) radius radius)))
