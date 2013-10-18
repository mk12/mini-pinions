(ns mini-pinions.main
  (:use quil.core))

(def pos (atom 0))

(defn setup []
  (smooth))

(defn draw []
  (swap! pos inc)
  (background 200)
  (ellipse (/ (width) 2) @pos 20 20))

(defsketch main-sketch
  :title "Mini Pinions"
  :setup setup
  :draw draw
  :size [800 600])

(defn -main
  [& args]
  (main-sketch))
