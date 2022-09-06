import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration
import org.modelmapper.convention.MatchingStrategies

class CustomMapper() : ModelMapper() {
    init {
        configuration.matchingStrategy = MatchingStrategies.LOOSE
        configuration.fieldAccessLevel = Configuration.AccessLevel.PRIVATE
        configuration.isFieldMatchingEnabled = true
        configuration.isSkipNullEnabled = true
    }
}

object Mapper {
    val mapper = CustomMapper()

    inline fun <S, T> map(source: S, clazz: Class<T>): T = mapper.map(source, clazz)
}