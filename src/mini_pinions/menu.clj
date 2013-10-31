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

(def copyright "Copyright © 2013 Mitchell Kember.\nSubject to the MIT License.")
(def copyright-size 18)
(def copyright-pos (v/make c/half-width (- c/height 80)))

(def instructions-size 20)
(def instructions-lines
  ["Fledge is a special bird. His pinions (wings) are so mini he can't fly."
   "Instead, he has to take advantage of gravity to slide and gain speed."
   ""
   "Control Fledge by CLICKING THE MOUSE to make him nosedive,"
   "and time it just right to SLIDE along a valley each time."
   "Get to the end of the level with as high a score as possible!"
   ""
   "For serious points, reach escape velocity and visit OUTER SPACE!"
   "You'll get 10 times more points, but watch out! Planets exert their own"
   "gravitational pull, and if you're not careful you'll crash into one,"
   "fall down, and take a 500 point penalty."])

(def scores-size 30)

;;;;; Menu button

(defn menu-action
  "Returns to the main menu, passing along cached data."
  [world]
  (c/init {:name :menu
           :game (:game world)
           :high-scores (:high-scores world)}))

(def menu-button
  (b/make-control "<" menu-action b/top-left-1 [255 255 255]))

;;;;; Buttons

(defmacro returning-action
  "Makes an action that will go to the given world, passing along cached data."
  [world-name]
  `(fn [world#] {:name ~world-name
                 :game (:game world#)
                 :high-scores (:high-scores world#)}))

(defn play-action [world]
  "Creates (or retrieves) the game world when the play button is pressed."
  (or (:game world)
      (c/init {:name :game
               :level 1
               :high-scores (:high-scores world)})))

(def main-buttons
  (b/make-button-stack
    (v/make c/half-width c/half-height)
    (v/make 400 300)
    button-margin
    [{:text "Play"
      :action play-action
      :color [255 150 0]}
     {:text "Levels"
      :action (returning-action :level-select)
      :color [100 100 100]}
     {:text "Instructions"
      :action (returning-action :instructions)
      :color [0 221 255]}
     {:text "Scores"
      :action (returning-action :scores)
      :color [200 200 200]}]))

(defmacro level-action [level]
  `(fn [world#]
     (let [game# (:game world#)]
       (if (= (:level game#) ~level)
         game#
         (c/init {:name :game
                  :level ~level
                  :high-scores (:high-scores world#)})))))

(def level-buttons
  (conj
    (b/make-button-grid
      :rect
      (v/make c/half-width c/half-height)
      (v/make 300 200)
      button-margin
      3
      (map #(hash-map :text (str %) :action (level-action %) :color [80 80 80])
           (range 1 7)))
    menu-button))

;;;;; Draw

(defn draw-title
  "Draws a title in large text in the top-center region."
  [title]
  (q/text-size 50)
  (c/fill-grey 0)
  (c/draw-text title (v/make c/half-width 75)))

;;;;; Menu world

(defmethod c/input :menu [world]
  (or (b/button-action main-buttons world)
      world))

(defmethod c/draw :menu [world]
  (c/clear-background background-color)
  (draw-title "Mini Pinions")
  (q/text-size copyright-size)
  (c/draw-text copyright copyright-pos)
  (b/draw-buttons main-buttons))

;;;;; Level select world

(defmethod c/input :level-select [world]
  (or (b/button-action level-buttons world)
      world))

(defmethod c/draw :level-select [world]
  (c/clear-background background-color)
  (draw-title "Level Select")
  (b/draw-buttons level-buttons))

;;;;; Instructions world

(defmethod c/input :instructions [world]
  (or (b/button-action [menu-button] world)
      world))

(defmethod c/draw :instructions [world]
  (c/clear-background background-color)
  (draw-title "Instructions")
  (b/draw-button menu-button)
  (q/text-size instructions-size)
  (doall
    (map-indexed
      (fn [index line]
        (c/draw-text line (v/make c/half-width (+ 150 (* index 40)))))
      instructions-lines)))

;;;;; Scores world

(defn score-list
  "Returns its argument unless it is nil, in which case it returns a list
  containing a zero for each level."
  [high-scores]
  (vec (or high-scores (repeat 6 0))))

(defn add-score
  "Adds a score to the list of high scores. Only changes if it is a new best."
  [level score high-scores]
  (let [v (score-list high-scores)
        index (- level 1)]
    (assoc v index (max (v index) score))))

(defmethod c/input :scores [world]
  (or (b/button-action [menu-button] world)
      world))

(defmethod c/draw :scores [world]
  (c/clear-background background-color)
  (draw-title "Scores")
  (b/draw-button menu-button)
  (q/text-size scores-size)
  (doall
    (map-indexed
      (fn [index score]
        (c/draw-text (str "Level " (+ index 1) ": " (int score))
                     (v/make c/half-width (+ 200 (* index 40)))))
      (score-list (:high-scores world)))))
