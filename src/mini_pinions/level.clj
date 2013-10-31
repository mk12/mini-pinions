;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.level
  "Defines all the level data so that it doesn't clutter up mini-pinions.game."
  (:require [quil.core :as q]
            [mini-pinions.planet :as p]
            [mini-pinions.curve :as u]
            [mini-pinions.vector :as v]))

(def levels
  [{:start (v/make 7000 400)
    :end 14500
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
       {:height 200
        :direction :up
        :half-cycles 60
        :width 15000}])
    :galaxy
    (p/make-galaxy
      1500
      800
      [{:center (v/make 20 30)
        :size 20
        :color [100 50 25]}
       {:center (v/make 50 -10)
        :size 30
        :color [255 255 255]}
       {:center (v/make 200 200)
        :size 100
        :color [100 200 255]}
       {:center (v/make 500 400)
        :size 75
        :color [255 200 210]}])}])

(defn level-n
  "Returns the level data for the nth level."
  [n]
  (levels (- n 1)))
