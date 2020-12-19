package edu.nitt.vortex21.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import edu.nitt.vortex21.viewmodel.UserViewModel
import edu.nitt.vortex21.viewmodel.ViewModelKey

@Module
abstract class UserModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel
}
