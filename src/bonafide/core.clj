(ns bonafide.core)

(declare concat-array)
(declare merge-with-concat)

(defn build-validation [attr options defaults condition]
  (let [options (if options (merge defaults options) defaults)]
    (partial (fn [attr options attrs]
               (if (condition (attrs attr) options)
                 attrs
                 (merge-with merge-with-concat attrs {:errors {attr (options :message)}})
                 ))
             attr options)))

(defn- merge-with-concat [& args]
  (apply merge-with concat-array args))

(defn- concat-array [& args]
  (flatten (into [] (concat args))))
