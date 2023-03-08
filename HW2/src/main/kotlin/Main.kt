import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

enum class Location {
    ROOM, KITCHEN, TOILET, BATHROOM
}

// SmartDevices:
// TvDevice
// - change channel
// - change volume
// LightDevice
// - can light on/off.
// - there is light volume controller.
// ThermoPot
// - (re)boil water
// - choose temperature
// Thermometer (one per location)

abstract class SmartDevice {
    abstract val id: String
    protected open lateinit var location: Location
    abstract var name: String

    private var isBroken: Boolean = false
    private var isEnabled: Boolean = false
        set(value) {
            if ((!value) || (!isBroken)) {
                field = value
                println(this)
            } else println("Can't turn on the $deviceInfo. It is broken.")
        }

    abstract val deviceType: String
    protected val deviceInfo: String
        get() = "$deviceType \"$name\" (id: $id)"

    protected fun isEnabled(): Boolean {
        if (!isEnabled)
            println("The $deviceInfo is off.")
        return isEnabled
    }

    abstract val mainStatement: String
    fun getState(): String =
        if (isBroken) "The $deviceInfo is broken."
        else if (!isEnabled) "The $deviceInfo is off."
        else mainStatement

    fun turnOn() {
        isEnabled = true
    }

    fun turnOff() {
        isEnabled = false
    }
}

class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int
) : ReadWriteProperty<Any?, Int> {

    private var fieldData = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return fieldData
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}

class TvDevice(
    override val id: String,
    override var location: Location,
    override var name: String = "MyTv"
) : SmartDevice() {
    private var speakerVolume by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)
    private var channelNumber by RangeRegulator(initialValue = 1, minValue = 0, maxValue = 200)

    override val deviceType: String = "TV"
    override val mainStatement: String = "The $deviceInfo is on.\n" +
            "Speaker volume set to $speakerVolume\n" +
            "and channel number is set to $channelNumber."

    fun increaseSpeakerVolume() {
        if (isEnabled()) {
            speakerVolume++
            println("$deviceInfo: Speaker volume increased to $speakerVolume.")
        }
    }

    fun nextChannel() {
        if (isEnabled()) {
            channelNumber++
            println("$deviceInfo: Channel number increased to $channelNumber.")
        }
    }

    fun decreaseSpeakerVolume() {
        if (isEnabled()) {
            speakerVolume--
            println("$deviceInfo: Speaker volume decreased to $speakerVolume.")
        }
    }

    fun previousChannel() {
        if (isEnabled()) {
            channelNumber--
            println("$deviceInfo: Channel number decreased to $channelNumber.")
        }
    }
}


class LightDevice : SmartDevice {

}

class ThermoPot : SmartDevice {

}

class Thermometer : SmartDevice {

}

class SmartHome(
    val id: String,
    val name: String
    // other properties here
) {
    private val devices: List<SmartDevice> = mutableListOf()
    fun register(d: SmartDevice, location: String): Unit = TODO()
    fun unregister(d: SmartDevice): Unit = TODO()
    fun listDevices(): List<SmartDevice> = TODO()
    fun health(): Boolean = TODO("Check health of devices")

    fun deviceById(id: String): SmartDevice = TODO()
    fun deviceByLocation(location: Location): List<SmartDevice> = TODO()

    // other methods here

}

class SmartHomeController {
    private var idToHomeMap = HashMap<String, SmartHome>()
    private var idToPasswordMap = HashMap<String, String>()

    private fun checkPassword(id: String): Boolean {
        print("Enter your password: ")
        val inputPassword = readlnOrNull()

        if (inputPassword != idToPasswordMap[id]) {
            println("Wrong password.")
            return false
        }
        return true
    }

    private fun isIdInMap(id: String): Boolean {
        if (id !in idToHomeMap.keys) {
            println("No home with this id was registered.")
            return false
        }
        return true
    }

    private fun makeNewPassword(): String? {
        println("Make up a password to access your home by id (password must be at least 6 characters long):")
        val inputPassword = readlnOrNull()

        if ((inputPassword?.length ?: 0) < 6) {
            println("Incorrect password. Password must be at least 6 characters long!")
            return null
        }
        return inputPassword
    }

    private fun changePassword(id: String) {
        val errorPhrase = "The password could not be changed. Please try again."

        if (!(checkPassword(id))) {
            println(errorPhrase)
            return
        }

        val newPassword = makeNewPassword()
        if (newPassword == null) {
            println(errorPhrase)
            return
        }

        idToPasswordMap[id] = newPassword
        println("The password has been successfully changed!")
    }

    fun registerHome(id: String, name: String = "MyHome"): SmartHome? {
        val errorPhrase = "The home could not be registered. Please try again."

        if (id in idToHomeMap.keys) {
            println("The house with this id is already registered.")
            println(errorPhrase)
            return null
        }

        val password = makeNewPassword()
        if (password == null) {
            println(errorPhrase)
            return null
        }

        idToHomeMap[id] = SmartHome(id, name)
        idToPasswordMap[id] = password
        println("Your home with an id \"$id\" has been successfully registered!")
        return idToHomeMap[id]
    }

    fun unregisterHome(id: String) {
        if (!isIdInMap(id))
            return

        if (checkPassword(id)) {
            idToHomeMap.remove(id)
            idToPasswordMap.remove(id)
            println("Your home with an id \"$id\" has been successfully unregistered!")
        }
    }

    fun getHome(id: String): SmartHome? {
        if (!isIdInMap(id))
            return null

        if (checkPassword(id))
            return idToHomeMap[id]
        return null
    }
}

fun main() {
    TvDevice("2",Location.BATHROOM).location=Location.KITCHEN
}