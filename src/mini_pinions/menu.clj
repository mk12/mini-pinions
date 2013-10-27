;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.menu
  "Implements the main menu world, the starting point of the application."
  (:require [quil.core :as q]
            [mini-pinions.button :as b]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v]))

;;;;; Constants

(def background-color [150 200 200])
(def button-margin 20)

;;;;; Buttons

(def menu-button
  (b/make-button :circle
                 "←"
                 #(c/make-world :menu)
                 (v/make 30 30)
                 (v/make 20 20)
                 [180 210 200]))

(def buttons
  (b/make-button-stack
    (v/make c/half-width c/half-height)
    (v/make 400 300)
    button-margin
    [{:text "Play"
      :action #(c/init {:name :game, :level 1})
      :color [50 50 50]}
     {:text "Instructions"
      :action #(c/init {:name :instructions})
      :color [255 255 255]}
     {:text "Levels"
      :action #(c/init {:name :level-select})
      :color [50 50 50]}
     {:text "Level Editor"
      :action #(c/init {:name :level-editor})
      :color [255 255 255]}]))

;;;;; Draw

(defn draw-title [s]
  (q/text-size 50)
  (c/draw-text s (v/make c/half-width 50)))

;;;;; Menu world

(defmethod c/input :menu [world]
  (if-let [btn (b/selected-button buttons)]
    ((:action btn))
    world))

(defmethod c/draw :menu [world]
  (c/clear-background background-color)
  (q/text-size 50)
  (draw-title "Mini Pinions")
  (b/draw-buttons buttons))

;;;;; Instructions world

(defmethod c/input :instructions [world]
  (if-let [btn (b/selected-button [menu-button])]
    ((:action btn))
    world))

(defmethod c/draw :instructions [world]
  (c/clear-background background-color)
  (b/draw-button menu-button)
  (draw-title "Instructions")
  (q/text-size 16)
  (c/draw-text "Later on, there will be instructions here."
               (v/make c/half-width 300)))

;;;;; Level select world

(defmethod c/input :level-select [world]
  (if-let [btn (b/selected-button [menu-button])]
    ((:action btn))
    world))

(defmethod c/draw :level-select [world]
  (c/clear-background background-color)
  (b/draw-button menu-button)
  (draw-title "Level Select"))
