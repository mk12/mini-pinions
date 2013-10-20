(ns mini-pinions.vector
  (:require [clojure.math.numeric-tower :as m]))

(defn add [u v]
  (map + u v))

(defn sub [u v]
  (map - u v))

(defn scale [k v]
  (map #(* k %) v))

(defn dot [u v]
  (reduce + (map * u v)))

(defn norm [v]
  (m/sqrt (dot v v)))

(defn normSq [v]
  (dot v v))

(defn normalize [v]
  (scale (/ (norm v)) v))

(defn negate [v]
  (scale -1 v))
