(ns onyx-blueprint.ui.simulator
  (:require [cljs.pprint :as pprint]
            [om.dom :as dom]
            [om.next :as om :refer-macros [defui]]
            [onyx-blueprint.extensions :as extensions]
            [onyx-blueprint.ui.code-editor :as code-editor]
            [onyx-local-rt.api :as onyx.api]))

(defn button [label cb]
  (dom/input #js {:value label
                  :type "button"
                  :onClick cb}))

(defui Simulator
  static om/IQuery
  (query [this]
    [:component/id :component/type :evaluations/link])
  
  Object
  (render [this]
    (let [{:keys [component/id evaluations/link] :as props} (om/props this)
          init-data (-> link :init-data :result :value)
          job-env (-> link :job-env :result :value)
          env-summary (if job-env (onyx.api/env-summary job-env))
          transact! (partial om/transact! this)]
      (apply dom/div #js {:id (name id) :className "col component component-editor"}
               (cond-> []
                 (nil? job-env)
                 (conj (button "Initialize job"
                               (fn [evt]
                                 (.preventDefault evt)
                                 (transact! `[(onyx/init {:id ~id :job ~init-data})]))))
                 job-env
                 (conj (button "Reinitialize job"
                               (fn [evt]
                                 (.preventDefault evt)
                                 (transact! `[(onyx/init {:id ~id :job ~init-data})])))
                       (button "Generate input"
                               (fn [evt]
                                 (.preventDefault evt)
                                 (transact! `[(onyx/new-segment {:id ~id})])))
                       (button ">"
                               (fn [evt]
                                 (.preventDefault evt)
                                 (transact! `[(onyx/tick {:id ~id})])))
                       (button ">>"
                               (fn [evt]
                                 (.preventDefault evt)
                                 (transact! `[(onyx/drain {:id ~id})])))                       
                       (dom/pre nil (with-out-str (pprint/pprint env-summary))))
                 )))))

(def simulator (om/factory Simulator))

(defmethod extensions/component-ui :simulator/basic
  [props]
  (simulator props))
