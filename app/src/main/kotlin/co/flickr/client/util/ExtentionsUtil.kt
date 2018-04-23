@file:JvmName("ExtensionsUtils")

package co.flickr.client.util


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import co.flickr.client.app.presentation.ViewModelFactory
import org.reactivestreams.Publisher

fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)
fun <T> Publisher<T>.toLiveData(): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this)

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(): T = ViewModelProviders.of(this, ViewModelFactory).get(T::class.java)

