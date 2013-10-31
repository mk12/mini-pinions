;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.core
  "Gets the ball rolling by creating the Quil sketch and defining -main."
  (:gen-class)
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            (mini-pinions menu game)))

(defn setup []
  (q/rect-mode :corners)
  (q/text-align :center :center)
  (c/init-state :menu))

(defn -main []
  (q/sketch
    :title "Mini Pinions"
    :size [c/width c/height]
    :renderer :p2d
    :setup setup
    :mouse-pressed c/input-world
    :key-pressed c/input-world
    :draw c/update-and-draw-world))
