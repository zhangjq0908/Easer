package ryey.easer.core.ui.data

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*
import ryey.easer.commons.local_skill.InvalidDataInputException
import ryey.easer.commons.local_skill.conditionskill.ConditionData
import ryey.easer.core.data.ConditionStructure
import ryey.easer.core.data.storage.ConditionDataStorage

class StubEditDataActivity(private val thowException: Boolean) : AbstractEditDataActivity<ConditionStructure, ConditionDataStorage>() {

    private var mockData: ConditionData = mock(ConditionData::class.java)
    private var mockStorage: ConditionDataStorage = mock(ConditionDataStorage::class.java)

    init {
        `when`(mockData.isValid).thenReturn(true)
        `when`(mockStorage.add(any<ConditionStructure>())).thenReturn(true)
        purpose = EditDataProto.Purpose.add
        storage = mockStorage
    }

    override fun retDataStorage(): ConditionDataStorage {
          throw NotImplementedError()
    }

    override fun contentViewRes(): Int {
        throw NotImplementedError()
    }

    override fun init() {
        throw NotImplementedError()
    }

    override fun loadFromData(data: ConditionStructure?) {
    }

    override fun saveToData(): ConditionStructure {
        if (thowException)
            throw Exception()
        return ConditionStructure("test", mockData)
    }

    override fun title(): String {
        throw NotImplementedError()
    }
}

class AbstractEditDataActivityTest {
    @Test
    fun testTryPersistChangeReturnsInvalidDataInputExceptionWhenSaveToDataThrowsException() {
        val editDataActivity = StubEditDataActivity(true)
        assertEquals(InvalidDataInputException(java.lang.Exception()).toString(), editDataActivity.tryPersistChange().toString())
    }

    @Test
    fun testTryPersistChangeReturnsNullWhenSuccess() {
        val editDataActivity = StubEditDataActivity(false)
        assertNull(editDataActivity.tryPersistChange())
    }
}