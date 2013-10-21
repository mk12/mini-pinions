;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.menu
  "Implements the main menu World, the starting point of the application."
  (:require [quil.core :as q]
            [mini-pinions.common :as c]
            [mini-pinions.vector :as v]))

;;;;; Constants

(def background-color 120)
(def button-size (v/make 300 50))
(def button-margin 20)
(def first-button-y 250)
(def button-text-size 30)

;;;;; Buttons

(defn button-pos
  "Calculates the position of a button (top-left corner)."
  [index]
  (v/make (- c/center-x
             (/ (v/x button-size) 2))
          (+ first-button-y
             (* index (+ button-margin
                        (v/y button-size))))))

(defn calc-button
  "Adds some calculates properties to a button."
  [index btn]
  (let [pos (v/make
              (- c/center-x (/ (v/x button-size) 2))
              (+ first-button-y (* index (+ button-margin
                                           (v/y button-size)))))]
    (assoc btn
            :pos pos
            :text-pos (v/add pos (v/div 2 button-size))
            :hover-color (map #(* 0.7 %) (:color btn)))))

(def buttons
  (map-indexed
    calc-button
    [{:text "Play"
      :world :game
      :color [50 180 255]}
     {:text "Instructions"
      :world :instructions
      :color [255 100 50]}
     {:text "Levels"
      :world :level-selector
      :color [50 180 50]}]))

(defn mouse-in-button?
  "Returns true if the mouse cursor is inside the given button."
  [btn]
  (let [p1 (:pos btn)
        p2 (v/add p1 button-size)
        x (q/mouse-x)
        y (q/mouse-y)]
    (and (>= x (v/x p1))
         (<= x (v/x p2))
         (>= y (v/y p1))
         (<= y (v/y p2)))))

(defn selected-button
  "Returns the button under the mouse cursor if there is one and the mouse
  button is pressed, otherwise returns nil."
  []
  (if (q/mouse-state)
    (first (filter mouse-in-button? buttons))))

(defn draw-buttons
  "Draws all of the menu buttons with rectangles and text."
  []
  (doseq [btn buttons]
    (q/push-style)
    (apply q/fill (if (mouse-in-button? btn)
                    (:hover-color btn)
                    (:color btn)))
    (c/draw-rect (:pos btn) button-size)
    (q/fill 255)
    (q/text-align :center :center)
    (q/text-size button-text-size)
    (c/draw-text (:text btn) (:text-pos btn))
    (q/pop-style)))

;;;;; World

(defmethod c/input :menu [world]
  (if-let [btn (selected-button)]
    (c/make-world (:world btn))
    world))

(defmethod c/draw :menu [world]
  (q/background background-color)
  (q/text (str (q/current-frame-rate)) 20 20)
  (draw-buttons))
