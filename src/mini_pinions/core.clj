(ns mini-pinions.core
  (:use quil.core))

(def pos (atom 0))

(defn setup []
  (smooth)
  (background 200))

(defn draw []
  (swap! pos inc)
  (ellipse (/ (width) 2) @pos 20 20))

(defsketch main-sketch
  :title "Mini Pinions"
  :setup setup
  :draw draw
  :size [500 500])

(defn -main [& args]
  (main-sketch))
