(ns making-change.core-test
  (:require [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [making-change.core :refer :all]))

(def
  ^{:doc "Generate a set of available coins; all sets include the coin 1"}
  gen-coinset
  (gen/fmap #(into #{1} %) (gen/vector gen/s-pos-int)))

(def
  ^{:doc "Generate an amount and coinset, where the amount is one of the coins"}
  gen-amount-from-coinset
  (gen/bind gen-coinset #(gen/tuple (gen/return %) (gen/elements %))))

;; Change must add up -- or else, *gasp!* -- the function is committing fraud!
(defspec correct-change
         (prop/for-all [coinset gen-coinset
                        amount gen/s-pos-int]
                       (let [change (make-change coinset amount)]
                         (= amount (apply + change)))))

;; If the amount is one of the coins, that change must be a single coin
(defspec smallest-change
         (prop/for-all [[coinset amount] gen-amount-from-coinset]
                       (let [change (make-change coinset amount)]
                         (= 1 (count change)))))

;; The number of coins to change amount1+amount2 together must be no more than
;; the number of coins combined from changing amount1 and amount2 seperately.
(defspec combined-change
         (prop/for-all [coinset gen-coinset
                        amount1 gen/s-pos-int
                        amount2 gen/s-pos-int]
                       ;; NOTE: changing dp-change to greedy-change fails spec
                       (let [f (partial make-change dp-change coinset)
                             change-seperate (concat (f amount1) (f amount2))
                             change-together (f (+ amount1 amount2))]
                         (<= (count change-together) (count change-seperate)))))
