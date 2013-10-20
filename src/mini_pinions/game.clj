;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.game
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v]))

(def radius 10)
(def k 0.001)

(defmethod c/init :game [world]
  (assoc world :pos [100.0 0.0] :vel [0.0 5.0]))

(defmethod c/update :game [world]
  (let [accel (v/scale (- k) (:pos world))
        new-vel (v/add accel (:vel world))]
    (assoc world
      :vel new-vel
      :pos (v/add new-vel (:pos world)))))

(defmethod c/draw :game [world]
  (let [pos (:pos world)]
    (q/background 100)
    (q/translate (/ (q/width) 2) (/ (q/height) 2))
    (q/ellipse (nth pos 0) (nth pos 1) radius radius)))
