;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.button
  "Manages making buttons, detecting clicks on them, and drawing them."
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v]))

;;;;; Constants

(def contrast-threshold 0.4)
(def button-hover-factor 0.3)

(def standard-size (v/make 30 30))
(def top-left-1 (v/make 30 30))
(def top-left-2 (v/make 70 30))
(def top-right-1 (v/make (- c/width 30) 30))
(def top-right-2 (v/make (- c/width 70) 30))

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

(defn make-control
  "Makes a standard-sized circular button."
  [text action center color]
  (make-button :circle text action center standard-size color))
  
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

(defn make-button-grid
  "Makes a grid of buttons given their shape (:rect or :circle), an enclosing
  rectangle (defined by center and size), an amount of padding between buttons,
  the number of columns, and a collection of maps with keys :text, :action, and
  :color."
  [shape center size padding n-cols button-defs]
  (let [n-buttons (count button-defs)
        n-rows (quot n-buttons n-cols)
        total-padding-x (* (- n-cols 1) padding)
        total-padding-y (* (- n-rows 1) padding)
        width (/ (- (v/x size) total-padding-x) n-cols)
        height (/ (- (v/y size) total-padding-y) n-rows)
        button-size (v/make width height)
        start (v/sub center (v/div 2 (v/sub size button-size)))
        delta-x-n #(* (+ width padding) (mod % n-cols))
        delta-y-n #(* (+ height padding) (quot % n-cols))
        center-n #(v/add start (v/make (delta-x-n %) (delta-y-n %)))
        centers (map center-n (range n-buttons))]
    (map #(make-button shape (:text %1) (:action %1) %2 button-size (:color %1))
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

;;;;; Action

(defmacro make-world [world-name]
  `(fn [_#] (c/init {:name ~world-name})))

(defmacro init-world [world]
  `(fn [_#] (c/init ~world)))

(defn button-action
  "If a button is selected, returns the result of applying its action to the
  given world, otherwise returns nil."
  [buttons world]
  (if-let [button (selected-button buttons)]
    ((:action button) world)))
