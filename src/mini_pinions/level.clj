;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.level
  "Defines all the level data so that it doesn't clutter up mini-pinions.game."
  (:require [quil.core :as q]
            [mini-pinions.planet :as p]
            [mini-pinions.curve :as u]
            [mini-pinions.vector :as v]))

(def levels
  [{:start (v/make 400 400)
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
        :half-cycles 600
        :width 150000}])
    :galaxy
    (p/make-galaxy
      100
      [{:center (v/make 20 30)
        :size 50
        :color [100 50 25]}])}])

(defn level-n
  "Returns the level data for the nth level."
  [n]
  (levels (- n 1)))
