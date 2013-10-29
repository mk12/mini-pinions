;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.planet
  "Implements the planets that appear when Fledge reaches outer space."
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v])
  (:import java.lang.Math))

;;;;; Constants

(def gravitational-constant 0.1)

;;;;; Planet

(defn make-planet
  "Makes a new planet given its position, size (diameter), and color."
  [center size color]
  (let [radius (/ size 2)]
    {:center center
     :radius radius
     :mass (* Math/PI (c/square radius))
     :color color}))

(defn planet-attraction
  "Returns the acceleration due to the force of attraction towards a planet."
  [pos planet]
  (let [connect (v/sub (:center planet) pos)
        dist (v/norm connect)]
    (v/scale (* gravitational-constant
                (:mass planet)
                (/ (Math/pow dist 3)))
             connect)))

(defn planet-collision?
  "Returns true if the circle is colliding with the planet."
  [pos radius planet]
  (< (c/square (+ radius (:radius planet)))
     (v/norm-sq (v/sub (:center planet) pos))))

(defn draw-planet
  "Draws the planet as a circle."
  [planet]
  (c/fill-color (:color planet))
  (c/draw-circle (:center planet) (:radius planet)))

;;;;; Galaxy

;;; A "planet definition" refers to the keys
;;;     :center, :size, :color.
;;; A "planet" refers to the keys
;;;     :center, :radius, :mass, :color.
;;; A "galaxy" refers to a collection of planets.

(defn make-planet-translated
  "Make a planet that is translated to the right by x."
  [x planet-def]
  (make-planet (v/add (:center planet-def) (v/make x 0))
               (:size planet-def)
               (:color planet-def)))

; TODO: add axis which is added to all y's.
(defn make-galaxy
  "Makes a galaxy by repeating a collection of planets definitions forever. The
  separating space between successive repetitions of the planet collection is
  given by sep."
  [sep planet-defs]
  (let [last-planet (last planet-defs)
        width (+ (v/x (:center last-planet)) (:radius last-planet) sep)]
    (concat
      (map-indexed
        (fn [index p-defs]
          (map (partial make-planet-translated (* index width)) p-defs))
        (repeat planet-defs)))))

(defn net-attraction
  "Returns the sum of the accelerations caused by each planet."
  [pos galaxy]
  (reduce v/add
          (map (partial planet-attraction pos) galaxy)))

(defn colliding-planet
  "Returns the planet that is being collided with, or nil if there is none."
  [pos radius galaxy]
  (first (filter (partial planet-collision? pos radius) galaxy)))

(defn subgalaxy
  "Returns the planets in the galaxy that lie within the given bounds."
  [[start end] galaxy]
  (take-while
    #(> end (- (v/x (:center %)) (:radius %)))
    (drop-while
      #(> start (+ (v/x (:center %)) (:radius %))
      galaxy))))

(defn draw-galaxy
  "Draws all of the planets in a galaxy."
  [galaxy]
  (doseq [planet galaxy] (draw-planet planet)))
