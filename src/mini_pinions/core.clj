;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.core
  "Gets the ball rolling by creating the Quil sketch and defining -main."
  (:gen-class)
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            (mini-pinions menu game)))

(defn setup []
  (q/smooth)
  (q/ellipse-mode :radius)
  (q/rect-mode :corners)
  (c/init-state :menu))

(q/defsketch sketch
  :title "Mini Pinions"
  :size [c/width c/height]
  :renderer :opengl
  :setup setup
  :mouse-pressed c/input-world
  :key-pressed c/input-world
  :draw c/update-and-draw-world)

(defn -main [] (sketch))
