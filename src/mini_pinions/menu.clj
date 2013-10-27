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

(def buttons
  (b/make-button-stack
    (v/make c/half-width c/half-height)
    (v/make 400 300)
    button-margin
    [{:text "Play"
      :action {:name :game, :level 1}
      :color [50 50 50]}
     {:text "Instructions"
      :action {:name :instructions}
      :color [255 255 255]}
     {:text "Levels"
      :action {:name :level-selector}
      :color [50 50 50]}
     {:text "Level Editor"
      :action {:name :level-editor}
      :color [255 255 255]}]))

;;;;; World

(defmethod c/input :menu [world]
  (if-let [btn (b/selected-button buttons)]
    (c/init (:action btn))
    world))

(defmethod c/draw :menu [world]
  (c/clear-background background-color)
  (b/draw-buttons buttons))
