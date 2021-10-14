(ns practicalli.status-monitor-service
  (:gen-class)
  (:require [org.httpkit.server       :as app-server]
            [compojure.core           :refer [defroutes GET]]
            [compojure.route          :refer [not-found]]
            [ring.handler.dump        :refer [handle-dump]]
            [ring.util.response       :refer [response]]
            [practicalli.helpers-http :refer [http-status-code]]))

(defonce app-server-instance (atom nil))

(defn dashboard
  [request]
  (response "Status Monitor Dashboard"))

(defroutes status-monitor
  (GET "/" [] dashboard)
  (GET "/request-dump" [] handle-dump)
  )

(defn stop-app-server
  "Gracefully shutdown the server, waiting 100ms"
  []
  (when-not (nil? @app-server-instance)
    (@app-server-instance :timeout 100)
    (reset! app-server-instance nil)
    (println "INFO: Application server stopped")))

(defn -main
  "Start the application server and run the application"
  [port]
  (println "INFO: Starting server on port: " port)

  (reset! app-server-instance
          (app-server/run-server #'status-monitor {:port (Integer/parseInt port)})))

(defn restart-app-server
  "Convenience function to stop and start the application server"
  []
  (stop-app-server)
  (-main "8080"))

(comment

  ;; start application
  (-main "8080")

  ;; stop application
  (stop-app-server)

  ;; restart application
  (restart-app-server)

  )
