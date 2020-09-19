package Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myfirstofficeappecommerce.Models.VariantsModelClass

@Dao
interface MyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(variantsModelClass: VariantsModelClass)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(variantsModelClass: VariantsModelClass)

    @Delete
    fun delete(variantsModelClass: VariantsModelClass)

    @Query("SELECT * FROM VariantsModelClass")
    fun getAllVariantData(): LiveData<List<VariantsModelClass>>

    @Query("SELECT * FROM VariantsModelClass WHERE id=:id")
    fun getAllVariantDataByID(id: Int): VariantsModelClass

    @Query("SELECT * FROM VariantsModelClass WHERE isfav=:fav")
    fun getAllFavVariants(fav: Boolean): LiveData<List<VariantsModelClass>>
}