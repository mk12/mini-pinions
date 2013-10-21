;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.game
  "Implements the game World, where all the fun happens."
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v])
  (:import java.lang.Math))

;;;;; Constants

(def radius 10)
(def g-fly 1)
(def g-fall 3)

(def path-resolution 3)

;;;;; Levels

(defn segment-y
  "Calculates the y-value of a segment given an x-value."
  [seg x]
  (* (:amplitude seg)
     (Math/cos (* (:k seg)
                  (- x (:phase seg))))))

(defn calc-segments-k
  "Calculates the horizontal compression factors for each segment."
  [segs]
  (map #(/ (* Math/PI 2) (:period %)) segs))

(defn calc-segments-start
  "Calculates the x-value of the first point of each segment"
  [segs]
  (reductions
    (fn [acc s]
      (+ acc (* (:cycles s) (:period s))))
    0
    segs))

(defn calc-segments-axis
  "Calculates the y-value of the first point of each segment."
  [segs]
  (reductions
    (fn [acc s]
      (+ acc (segment-y (* (:cycles s) (:period s)))))
    0
    segs))

(defn calc-segments
  "Adds some calculated values to a vector of segments."
  [segs]
  (let [ks (calc-segments-k segs)
        starts (calc-segments-start segs)
        new-segs (map #(assoc %1 :k %2 :start %3) segs ks starts)]
    (map #(assoc %1 :axis %2)
         new-segs
         (calc-segments-axis new-segs))))

(def level-1
  {:start 100
   :segments
   (calc-segments
     [{:amplitude 5
       :period 300
       :phase 0
       :cycles 0.5}
      {:amplitude 3
       :period 100
       :phase 0
       :cycles 5}])})

(defn path-y
  [level x]
  (let [seg (last (take-while #(> (:end %) x) (:segments level)))]))


;;;;; Bird

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
    (q/background 100)
    (q/ellipse c/center-x (v/y pos) radius radius)))
