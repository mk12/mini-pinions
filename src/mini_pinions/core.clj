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
  (c/init-state)
  (c/show-world :menu))

(q/defsketch sketch
  :title "Mini Pinions"
  :size [800 600]
  :renderer :opengl
  :setup setup
  :draw c/draw-world)

(defn -main [& args] (sketch))
