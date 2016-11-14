(ns onyx-tutorial.windows
  (:require [cljs.pprint :as pprint]
            [goog.dom :as gdom]
            [onyx-blueprint.api :as api]
            [onyx-tutorial.builders :as b]))

(def components
  [(b/header ::title
             "Discussion")

   (b/hiccup
    ::discussion
    [:div
     [:p "Windowing splits up a possibly unbounded data set into finite, possibly overlapping portions. Windows allow us create aggregations over distinct portions of a stream, rather than stalling and waiting for the entire data data set to arrive. In Onyx, Windows strictly describe how data is accrued. When you want to do something with the windowed data, you use a Trigger."]     
     [:p "Onyx’s windowing mechanisms are strong enough to handle stream disorder. If your data arrives in an order that isn’t \"logical\" (for example, " [:code ":event-time"] " keys moving backwards in time), Onyx can sort out the appropriate buckets to put the data in."]
     [:p "The topic of windows has been widely explored in academic literature, so if you're interested in reading about the concept in general, there are plenty of great papers to read. There are different types of windows. Onyx supports Fixed, Sliding, Global, and Session windows. We'll explain each of these and show them in action."]])

   (b/hiccup ::window-types-note
             [:aside
              [:p "Onyx supports Fixed, Sliding, Global, and Session windows."]])

   (b/header ::fixed-windows-header "Fixed Windows")

   (b/hiccup
    ::fixed-windows-discussion
    [:div
     [:p "Fixed windows, sometimes called Tumbling windows, span a particular range and do not slide. That is, fixed windows never overlap one another. Consequently, a data point will fall into exactly one instance of a window (often called an extent in the literature). As it turns out, fixed windows are a special case of sliding windows where the range and slide values are equal."]])

   {:component/id ::fixed-window-example
    :component/type :editor/data-structure
    :evaluations/init :content/default-input
    :content/default-input
    [{:window/id :collect-segments
      :window/task :identity
      :window/type :fixed
      :window/aggregation :onyx.windowing.aggregation/count
      :window/window-key :event-time
      :window/range [5 :minutes]}]}

   {:component/id ::fixed-window-data-example
    :component/type :editor/data-structure
    :evaluations/init :content/default-input
    :content/default-input
    [{:id 0 :event-time 100}
     {:id 1 :event-time 200}
     {:id 2 :event-time 200}
     {:id 3 :event-time 200}
     {:id 4 :event-time 200}
     {:id 5 :event-time 200}]}

   (b/hiccup
    ::fixed-window-output
    [:div
     [:p "The window visualization will go here."]])])

(def sections
  [{:section/id :windows-and-triggers
    :section/layout [[::title]
                     [::discussion ::window-types-note]
                     [::fixed-windows-header]
                     [::fixed-windows-discussion]
                     [::fixed-window-example ::fixed-window-data-example]
                     [::fixed-window-output]]}])

(api/render-tutorial! components sections (gdom/getElement "app"))
