;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.button
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v]))

;;;;; Factories

(defn make-button
  "Makes a new button and adds some calculated properties."
  [text action center size color]
  (let [half-size (v/div 2 size)]
    {:text text
     :action action
     :center center
     :top-left (v/sub center half-size)
     :bottom-right (v/add center half-size)
     :color color
     :hover-color (map #(* 0.7 %) color)
     :text-size (/ (v/y size) 2)
     :text-color (if (> (reduce + color) (* 255 3 0.5)) 0 255)}))
  
(defn make-button-stack
  "Makes a stack of buttons on top of each other given an enclosing rectangle
  (defined by center and size), an amount of padding between buttons, and a
  sequence of maps with the keys :text, :action, and :color."
  [center size padding button-defs]
  (let [n-buttons (count button-defs)
        total-padding (* (- n-buttons 1) padding)
        height (/ (- (v/y size) total-padding) n-buttons)
        button-size (v/make (v/x size) height)
        start (v/add (v/sub center (v/div 2 size))
                     (v/make 0 (/ height 2)))]
    (map #(make-button (:text %1) (:action %1) (c/dbg %2) button-size (:color %1))
         button-defs
         (map #(v/add (v/make 0 (* (+ height padding) %))
                      start)
              (range n-buttons)))))

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
  "Draws the button with a rectange and text."
  [button]
  (q/push-style)
  (apply q/fill (if (mouse-in-button? button)
                  (:hover-color button)
                  (:color button)))
  (c/draw-rect (:top-left button) (:bottom-right button))
  (q/fill (:text-color button))
  (q/text-align :center :center)
  (q/text-size (:text-size button))
  (c/draw-text (:text button) (:center button))
  (q/pop-style))

(defn draw-button-seq
  "Draws all of the buttons in a sequence using draw-button."
  [buttons]
  (doseq [btn buttons] (draw-button btn)))
