(ns onyx-blueprint.validation
  (:require [cljs.spec :as s]
            [onyx.spec :as ospec]
            [onyx-blueprint.extensions :as extensions]))

(defmethod extensions/validate :default
  [spec value]
  (if (and (not (nil? value)) (s/valid? spec value))
    {:valid? true}
    {:valid? false
     :explain (s/explain-str spec value)}))
