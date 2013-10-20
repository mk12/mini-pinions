(ns mini-pinions.core
  (:gen-class)
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            (mini-pinions menu game)))

(defn setup []
  (q/smooth)
  (q/ellipse-mode :radius)
  (c/init-state)
  (c/show-view :menu))

(q/defsketch sketch
  :title "Mini Pinions"
  :size [800 600]
  :renderer :opengl
  :setup setup
  :draw c/draw-current-view
  :mouse-pressed c/mouse-current-view
  :key-pressed c/key-current-view)

(defn -main [& args] (sketch))
