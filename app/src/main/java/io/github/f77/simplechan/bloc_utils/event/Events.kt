package io.github.f77.simplechan.bloc_utils.event

class Events {
    companion object {
        fun initialized(): InitializedEventInterface = object : InitializedEventInterface {}

        fun itemMoved(fromPosition: Int, toPosition: Int): ItemMovedEventInterface {
            return object : ItemMovedEventInterface {
                override val fromPosition: Int
                    get() = fromPosition
                override val toPosition: Int
                    get() = toPosition
            }
        }

        fun itemSwipedLeft(position: Int): ItemSwipedLeftEventInterface {
            return object : ItemSwipedLeftEventInterface {
                override val position: Int
                    get() = position
            }
        }

        fun itemSwipedRight(position: Int): ItemSwipedRightEventInterface {
            return object : ItemSwipedRightEventInterface {
                override val position: Int
                    get() = position
            }
        }
    }
}
