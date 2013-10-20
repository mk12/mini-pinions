(ns mini-pinions.menu
  (:require [quil.core :as q]
            [mini-pinions.common :as c]))

(defmethod c/init-view :menu [menu] menu)
(defmethod c/init-world :menu [_] nil)

(defmethod c/draw :menu [_]
  (q/background 200)
  (q/text (str (q/current-frame-rate)) 20 20))

(defmethod c/mouse-pressed :menu [_]
  (c/show-view :game))
