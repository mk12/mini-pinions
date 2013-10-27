;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.button
  "Manages making buttons, detecting clicks on them, and drawing them."
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v]))

;;;;; Constants

(def contrast-threshold 0.4)
(def button-hover-factor 0.3)

;;;;; Factories

(defn make-button
  "Makes a new button and adds some calculated properties."
  [shape text action center size color]
  (let [half-size (v/div 2 size)
        dark (< (reduce + color) (* 255 3 contrast-threshold))
        sign (if dark 1 -1)
        hover-mult (+ 1 (* sign button-hover-factor))]
    {:shape shape
     :text text
     :action action
     :center center
     :top-left (v/sub center half-size)
     :bottom-right (v/add center half-size)
     :color color
     :hover-color (map #(* hover-mult %) color)
     :text-size (v/y half-size)
     :text-color (if dark 255 0)}))
  
(defn make-button-stack
  "Makes a stack of buttons on top of each other given an enclosing rectangle
  (defined by center and size), an amount of padding between buttons, and a
  collection of maps with the keys :text, :action, and :color."
  [center size padding button-defs]
  (let [n-buttons (count button-defs)
        total-padding (* (- n-buttons 1) padding)
        height (/ (- (v/y size) total-padding) n-buttons)
        button-size (v/make (v/x size) height)
        start-y (- (v/y center) (/ (- (v/y size) height) 2))
        start (v/make (v/x center) start-y)
        center-n #(v/add start (v/make 0 (* (+ height padding) %)))
        centers (map center-n (range n-buttons))]
    (map #(make-button :rect (:text %1) (:action %1) %2 button-size (:color %1))
         button-defs
         centers)))

;;;;; Mouse

(defn mouse-in-button?
  "Returns true if the mouse cursor is inside the button."
  [button]
  (let [p1 (:top-left button)
        p2 (:bottom-right button)
        x (q/mouse-x)
        y (q/mouse-y)]
    (and (>= x (v/x p1))
         (<= x (v/x p2))
         (>= y (v/y p1))
         (<= y (v/y p2)))))

(defn selected-button
  "Returns the button under the mouse cursor if there is one and the mouse
  button is pressed, otherwise returns nil."
  [buttons]
  (if (q/mouse-state)
    (first (filter mouse-in-button? buttons))))

;;;;; Draw

(defn draw-button
  "Draws the button with a rectange or ellipse and text."
  [button]
  (c/fill-color
    (if (mouse-in-button? button) (:hover-color button) (:color button)))
  (((:shape button) {:rect c/draw-rect :circle c/draw-ellipse})
    (:top-left button) (:bottom-right button))
  (c/fill-grey (:text-color button))
  (q/text-size (:text-size button))
  (c/draw-text (:text button) (:center button)))

(defn draw-buttons
  "Draws all of the buttons in a collection using draw-button."
  [buttons]
  (doseq [btn buttons] (draw-button btn)))
