;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.menu
  "Implements the main menu world, the starting point of the application."
  (:require [quil.core :as q]
            [mini-pinions.button :as b]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v]))

;;;;; Constants

(def background-color [160 240 255])
(def button-margin 20)

;;;;; Menu button

(defn menu-action [world]
  (c/init {:name :menu, :game (:game world)}))

(def menu-button
  (b/make-control "<" menu-action b/top-left-1 [255 255 255]))

;;;;; Buttons

(defmacro returning-action [world-name]
  `(fn [world#] {:name ~world-name :game (:game world#)}))

(defn play-action [world]
  (if-let [game (:game world)]
    game
    (c/init {:name :game, :level 1})))

(def buttons
  (b/make-button-stack
    (v/make c/half-width c/half-height)
    (v/make 400 300)
    button-margin
    [{:text "Play"
      :action play-action
      :color [255 150 0]}
     {:text "Instructions"
      :action (returning-action :instructions)
      :color [0 221 255]}
     {:text "Levels"
      :action (returning-action :level-select)
      :color [255 255 255]}
     {:text "Level Editor"
      :action (returning-action :level-editor)
      :color [100 100 100]}]))

;;;;; Draw

(defn draw-title [title]
  (q/text-size 50)
  (c/fill-grey 0)
  (c/draw-text title (v/make c/half-width 75)))

;;;;; Menu world

(defmethod c/input :menu [world]
  (or (b/button-action buttons world)
      world))

(defmethod c/draw :menu [world]
  (c/clear-background background-color)
  (draw-title "Mini Pinions")
  (b/draw-buttons buttons))

;;;;; Instructions world

(defmethod c/input :instructions [world]
  (or (b/button-action [menu-button] world)
      world))

(defmethod c/draw :instructions [world]
  (c/clear-background background-color)
  (draw-title "Instructions")
  (b/draw-button menu-button)
  (q/text-size 16)
  (c/draw-text "Later on, there will be instructions here."
               (v/make c/half-width 300)))

;;;;; Level select world

(defmethod c/input :level-select [world]
  (or (b/button-action [menu-button] world)
      world))

(defmethod c/draw :level-select [world]
  (c/clear-background background-color)
  (draw-title "Level Select")
  (b/draw-button menu-button))
