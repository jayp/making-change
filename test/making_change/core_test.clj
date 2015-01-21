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

;; The number of coins to change double the amount must be at most twice the
;; number of coins to change amount
(defspec double-amount-change
         (prop/for-all [coinset gen-coinset
                        amount gen/s-pos-int]
                       ;; NOTE: changing dp-change to greedy-change fails spec
                       (let [f (partial make-change dp-change coinset)
                             change (f amount)
                             double-change (f (* amount 2))]
                         (<= (count double-change) (* (count change) 2)))))
