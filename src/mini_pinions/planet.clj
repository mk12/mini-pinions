;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.planet
  "Implements the planets that appear when Fledge reaches outer space."
  (:require [quil.core :as q]
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

(defn planet-collision
  "Returns true if the circle is colliding with the planet."
  [pos radius planet]
  (< (c/square (+ radius (:radius planet)))
     (v/norm-sq (v/sub (:center planet) pos))))

;;;;; Galaxy

;;; A "planet definition" refers to the keys
;;;     :center, :size, :color.
;;; A "planet" refers to the keys
;;;     :center, :radius, :mass, :color.
;;; A "galaxy" refers to a collection of planets.

(defn make-galaxy
  "Makes a galaxy by repeating a collection of planets definitions forever. The
  separating space between successive repetitions of the planet collection is
  given by sep."
  [sep planet-defs]
  (map-indexed
    (fn [index p-def]
      (make-planet (v/add (:center p-def) v/zero) ; change this
                   (:size p-def)
                   (:color p-def)))
    (cycle planet-defs)))
