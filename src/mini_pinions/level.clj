;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.level
  "Defines all the level data so that it doesn't clutter up mini-pinions.game."
  (:require [quil.core :as q]
            [mini-pinions.planet :as p]
            [mini-pinions.curve :as u]
            [mini-pinions.vector :as v]))

(def levels
  [{:start (v/make 300 350)
    :end 129500
    :path
    (u/make-path
      v/zero
      1
      [{:height 300
        :direction :up
        :half-cycles 1
        :width 300}
       {:height 250
        :direction :down
        :half-cycles 500
        :width 130000}
       {:height 300
        :direction :down
        :half-cycles 1
        :width 300}])
    :galaxy
    (p/make-galaxy
      1500
      600
      [{:center v/zero
        :size 30
        :color [160 80 225]}
       {:center (v/make 0 600)
        :size 20
        :color [150 150 150]}
       {:center (v/make 400 200)
        :size 80
        :color [222 80 160]}
       {:center (v/make 950 -20)
        :size 20
        :color [200 200 0]}
       {:center (v/make 1250 150)
        :size 30
        :color [0 180 255]}
       {:center (v/make 1600 0)
        :size 40
        :color [255 160 0]}])}
   {:start (v/make 400 400)
    :end 96000
    :path
    (u/make-path
      v/zero
      5
      [{:height 300
        :direction :up
        :half-cycles 1
        :width 400}
       {:height 200
        :direction :down
        :half-cycles 1
        :width 300}
       {:height 300
        :direction :up
        :half-cycles 1
        :width 250}
       {:height 200
        :direction :down
        :half-cycles 21
        :width 6000}
       {:height 100
        :direction :down
        :half-cycles 1
        :width 150}
       {:height 200
        :direction :up
        :half-cycles 18
        :width 5000}
       {:height 300
        :direction :up
        :half-cycles 1
        :width 200}
       {:height 200
        :direction :down
        :half-cycles 30
        :width 6500}
       {:height 400
        :direction :down
        :half-cycles 1
        :width 500}])
    :galaxy
    (p/make-galaxy
      1500
      800
      [{:center (v/make 20 30)
        :size 20
        :color [70 200 255]}
       {:center (v/make 90 300)
        :size 55
        :color [200 200 200]}
       {:center (v/make 400 -10)
        :size 45
        :color [255 255 255]}
       {:center (v/make 900 200)
        :size 60
        :color [255 160 0]}
       {:center (v/make 1350 00)
        :size 75
        :color [190 255 0]}])}
   {:start (v/make 200 400)
    :end 49000
    :path
    (u/make-path
      v/zero
      97500
      [{:height 100
        :direction :up
        :half-cycles 1
        :width 200}
       {:height 100
        :direction :up
        :half-cycles 20
        :width 3000}
       {:height 200
        :direction :up
        :half-cycles 40
        :width 9000}
       {:height 300
        :direction :up
        :half-cycles 80
        :width 20000}
       {:height 100
        :direction :down
        :half-cycles 1
        :width 300}])
    :galaxy
    (p/make-galaxy
      1500
      500
      [{:center v/zero
        :size 10
        :color [220 220 220]}
       {:center (v/make 500 200)
        :size 40
        :color [80 200 80]}
       {:center (v/make 700 -15)
        :size 35
        :color [140 80 200]}
       {:center (v/make 1000 450)
        :size 20
        :color [80 200 200]}])}
   {:start (v/make 300 350)
    :end 73800
    :path
    (u/make-path
      v/zero
      20
      [{:height 200
        :direction :up
        :half-cycles 1
        :width 300}
       {:height 100
        :direction :down
        :half-cycles 1
        :width 275}
       {:height 350
        :direction :up
        :half-cycles 1
        :width 420}
       {:height 280
        :direction :down
        :half-cycles 4
        :width 1500}
       {:height 320
        :direction :down
        :half-cycles 1
        :width 400}
       {:height 250
        :direction :up
        :half-cycles 1
        :width 300}
       {:height 380
        :direction :down
        :half-cycles 1
        :width 500}])
    :galaxy
    (p/make-galaxy
      1500
      400
      [{:center v/zero
        :size 80
        :color [200 80 80]}
       {:center (v/make 300 10)
        :size 40
        :color [210 210 210]}
       {:center (v/make 500 20)
        :size 30
        :color [150 250 0]}
       {:center (v/make 900 400)
        :size 35
        :color [255 120 0]}])}
   {:start (v/make 400 300)
    :end 100000
    :path
    (u/make-path
      v/zero
      35
      [{:height 400
        :direction :up
        :half-cycles 1
        :width 280}
       {:height 300
        :direction :down
        :half-cycles 1
        :width 270}
       {:height 210
        :direction :up
        :half-cycles 2
        :width 390}
       {:height 290
        :direction :up
        :half-cycles 1
        :width 310}
       {:height 270
        :direction :down
        :half-cycles 1
        :width 200}
       {:height 200
        :direction :up
        :half-cycles 6
        :width 1150}
       {:height 120
        :direction :up
        :half-cycles 1
        :width 200}
       {:height 240
        :direction :down
        :half-cycles 1
        :width 210}])
    :galaxy
    (p/make-galaxy
      1500
      1000
      [{:center (v/make 100 300)
        :size 25
        :color [255 120 0]}
       {:center (v/make 200 -10)
        :size 30
        :color [0 90 250]}
       {:center (v/make 500 45)
        :size 5
        :color [240 255 0]}
       {:center (v/make 900 400)
        :size 30
        :color [180 240 240]}
       {:center (v/make 1500 0)
        :size 10
        :color [120 120 120]}])}
   {:start (v/make 300 400)
    :end 77000
    :path
    (u/make-path
      v/zero
      100
      [{:height 160
        :direction :up
        :half-cycles 1
        :width 200}
       {:height 140
        :direction :up
        :half-cycles 1
        :width 190}
       {:height 160
        :direction :down
        :half-cycles 1
        :width 200}
       {:height 140
        :direction :down
        :half-cycles 1
        :width 190}])
    :galaxy
    (p/make-galaxy
      1500
      800
      [{:center v/zero
        :size 28
        :color [255 120 0]}
       {:center (v/make 400 250)
        :size 40
        :color [255 200 0]}
       {:center (v/make 600 -10)
        :size 35
        :color [80 250 255]}
       {:center (v/make 700 400)
        :size 20
        :color [100 100 100]}])}])

(defn level-n
  "Returns the level data for the nth level."
  [n]
  (levels (- n 1)))
