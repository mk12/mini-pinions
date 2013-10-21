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

(defn calc-segments-end
  "Calculates the x-values of the right edges of the connected segments."
  [segs]
  (rest
    (reductions
      (fn [acc s]
        (+ acc (* (:cycles s) (:period s))))
      0
      segs)))

(defn calc-segments-axis
  "Calculates the y-values of the left edges of the connected segments."
  [segs]
  (reductions
    (fn [acc s]
      (+ acc (* (:amplitude s)
                (Math/cos (* (/ (* Math/PI 2) (:period s))
                             (- (:end s) (:phase s)))))))
    0
    segs))

(defn calc-segments
  "Adds some calculated values to a vector of segments."
  [segs]
  (let [ends (calc-segments-end segs)
        s-ends (map #(assoc %1 :end %2) segs ends)]
    (map #(assoc %1 :axis %2)
         s-ends
         (calc-segments-axis s-ends))))

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

;; (defn seg-y
;;   [seg x]
;;   (* (:amplitude seg)
;;      (Math/cos (* (:k seg)
;;                   (- x (:phase seg))))))

;; (defn path-y
;;   [level x]
;;   (let [seg (first (drop-while #(< (:end) x) (:segments level)))]


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
