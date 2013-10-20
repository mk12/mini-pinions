;;; Copyright 2013 Mitchell Kember. Subject to the MIT License.

(ns mini-pinions.menu
  (:require [quil.core :as q]
            [mini-pinions.common :as c]))

(defmethod c/init :menu [world] world)

(defmethod c/update :menu [world]
  (if (q/mouse-state)
    (c/show-world :game)
    world))

(defmethod c/draw :menu [world]
  (q/background 200)
  (q/text (str (q/current-frame-rate)) 20 20))
