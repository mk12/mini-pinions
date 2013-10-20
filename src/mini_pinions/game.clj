(ns mini-pinions.game
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v]))

(def radius 10)
(def k 0.001)

(defmethod c/init-view :game [game] game)

(defmethod c/init-world :game [_]
  {:pos [100.0 0.0]
   :vel [0.0 5.0]})

(defn update [world]
  (let [accel (v/scale (- k) (:pos world))
        new-vel (v/add accel (:vel world))]
    {:vel new-vel
     :pos (v/add new-vel (:pos world))}))


(defmethod c/draw :game [_]
  (let [world (swap! (c/world) update)
        pos (:pos world)]
    (q/background 100)
    (q/translate (/ (q/width) 2) (/ (q/height) 2))
    (q/ellipse (nth pos 0) (nth pos 1) radius radius)))
