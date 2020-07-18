package io.github.f77.simplechan.bloc_utils.event

class Events {
    companion object {
        fun initialized(): InitializedEventInterface = object : InitializedEventInterface {}

        fun itemClicked(position: Int): ItemClickedEventInterface {
            return object : ItemClickedEventInterface {
                override val position: Int
                    get() = position
            }
        }

        fun itemLongClicked(position: Int): ItemLongClickedEventInterface {
            return object : ItemLongClickedEventInterface {
                override val position: Int
                    get() = position
            }
        }

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

        fun simpleSnackBar(message: String): SimpleSnackBarEventInterface {
            return object : SimpleSnackBarEventInterface {
                override val message: String
                    get() = message
            }
        }
    }
}
