package io.github.f77.simplechan.bloc_utils.action.interfaces

/**
 * This class represents universal common actions.
 */
class Actions {
    companion object {
        fun itemChanged(position: Int): ItemChangedActionInterface {
            return object :
                ItemChangedActionInterface {
                override val position: Int
                    get() = position
            }
        }

        fun itemMoved(fromPosition: Int, toPosition: Int): ItemMovedActionInterface {
            return object :
                ItemMovedActionInterface {
                override val fromPosition: Int
                    get() = fromPosition
                override val toPosition: Int
                    get() = toPosition
            }
        }

        fun itemRemoved(position: Int): ItemRemovedActionInterface {
            return object :
                ItemRemovedActionInterface {
                override val position: Int
                    get() = position
            }
        }

        fun simpleSnackBar(message: String): SimpleSnackBarActionInterface {
            return object :
                SimpleSnackBarActionInterface {
                override val message: String
                    get() = message
            }
        }
    }
}
