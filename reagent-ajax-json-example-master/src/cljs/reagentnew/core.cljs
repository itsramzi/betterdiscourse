(ns reagentnew.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [ajax.core :refer [GET POST]]))

;; -------------------------
;; Views

(enable-console-print!)

(def state (reagent/atom nil))

(defn home-page []
  ;; Using a "form-2" style reagent component so I can do some initial setup, in
  ;; this case querying the API
  ; (GET "/ajax" {:params {}
  ;               :handler (fn [r] (reset! state r))
  ;               :error-handler (fn [r] (prn r))
  ;               :response-format :json
  ;               :keywords? true})
  (fn []
    [:div [:h2 "Welcome to reagentnew"]
     [:div [:a {:href "/about"} "go to about page"]
      [:span (str @state)]]]))

(defn about-page []
  [:div [:h2 "About reagentnew"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

(defn add-node []
[:div {:class 'resizable'}]
  [:div {:class 'resizers'}]
    [:div {:class 'resizer top-left'}]
    [:div {:class 'resizer top-right'}]
    [:div {:class 'resizer bottom-left'}]
    [:div {:class 'resizer bottom-right'}]
  
)


;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
