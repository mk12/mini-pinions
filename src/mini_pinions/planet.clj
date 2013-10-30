;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.planet
  "Implements the planets that appear when Fledge reaches outer space."
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v])
  (:import java.lang.Math))

;;;;; Constants

(def gravitational-constant 3)

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
  (< (v/norm-sq (v/sub (:center planet) pos))
     (c/square (+ radius (:radius planet)))))

(defn stop-collision
  "Returns a new position that will no longer be colliding with the planet."
  [pos radius planet]
  (let [connect (v/sub pos (:center planet))
        direction (v/normalize connect)
        dist (+ radius (:radius planet))]
    (v/add (v/scale dist direction) (:center planet))))

(defn draw-planet
  "Draws the planet as a circle."
  [planet alpha]
  (c/fill-color (:color planet) alpha)
  (c/draw-circle (:center planet) (:radius planet)))

;;;;; Galaxy

;;; A "planet definition" refers to the keys
;;;     :center, :size, :color.
;;; A "planet" refers to the keys
;;;     :center, :radius, :mass, :color.
;;; A "galaxy" refers to a collection of planets.

(defn make-planet-translated
  "Make a planet that is translated by the given vector."
  [v planet-def]
  (make-planet (v/add (:center planet-def) v)
               (:size planet-def)
               (:color planet-def)))

(defn make-galaxy
  "Makes a galaxy by repeating a collection of planets definitions forever. The
  separating space between successive repetitions of the planet collection is
  given by sep."
  [axis sep planet-defs]
  (let [last-planet (last planet-defs)
        width (+ (v/x (:center last-planet)) (/ (:size last-planet) 2) sep)]
    (flatten
      (map-indexed
        (fn [index p-defs]
          (map (partial make-planet-translated (v/make (* index width) axis))
               p-defs))
        (repeat planet-defs)))))

(defn net-attraction
  "Returns the sum of the accelerations caused by each planet."
  [pos galaxy]
  (reduce v/add
          v/zero
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
      #(> start (+ (v/x (:center %)) (:radius %)))
      galaxy)))

(defn draw-galaxy
  "Draws all of the planets in a part of the galaxy that is inside the bounds."
  [galaxy bounds alpha]
  (doseq [planet (subgalaxy bounds galaxy)] (draw-planet planet alpha)))
