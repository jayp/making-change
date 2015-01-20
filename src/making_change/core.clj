(ns making-change.core)

(declare memoize-dp-change)

(defn dp-change
  "Make change using dynamic programming - this makes the minimal change"
  [coinset amount]
  (if (contains? coinset amount)
    [amount]
    (let [valid-coinset (filter #(>= amount %) coinset)
          choices (map #(conj (memoize-dp-change coinset (- amount %)) %)
                       valid-coinset)
          coins-per-choice (map count choices)
          least-coins (apply min coins-per-choice)]
      (first (filter #(= (count %) least-coins) choices)))))

(def
  ^{:doc     "Gotta memoize, or we won't finish computing for non-trivial cases"
    :private true}
  memoize-dp-change
  (memoize dp-change))

(defn greedy-change
  "Make change greedily - this does not make the minimal change"
  [coinset amount]
  (let [sorted-coinset (reverse (sort coinset)) ; sort coins: highest to lowest
        greedy-reduce (reduce (fn [state coin]
                                (let [amount-remaining (first state)
                                      change (second state)
                                      num-coins (quot amount-remaining coin)]
                                  (if (zero? num-coins)
                                    state ; no change in state
                                    [(- amount-remaining (* num-coins coin))
                                     (concat change (repeat num-coins coin))])))
                              [amount []] ; initial state for reduce operation
                              sorted-coinset)]
    (second greedy-reduce)))

(defn make-change
  "Make minimal change for amount, given an infinite supply of coinset. The
  return value is simply a vector of (unsorted) coins that make the change."
  ([coinset amount]
    (make-change dp-change coinset amount))
  ([f coinset amount]
    {:pre [(set? coinset) ; coins must not repeat
           (contains? coinset 1) ; must include 1 to make all changes possible
           (every? pos? coinset) ; all coins in coinset must be positive
           (pos? amount)]} ; amount must be positive
    (f coinset amount)))
