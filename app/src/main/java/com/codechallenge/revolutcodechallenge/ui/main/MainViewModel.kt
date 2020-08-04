package com.codechallenge.revolutcodechallenge.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codechallenge.revolutcodechallenge.Calculator
import com.codechallenge.revolutcodechallenge.repository.Repository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val calculator: Calculator
) : ViewModel() {

    val state: MutableLiveData<ViewModelState> = MutableLiveData()
    var disposable: Disposable = Disposable.empty()
    private var coefficientTransformation: Float = 0F

    fun onAction(action: ViewModelAction) {
        when (action) {
            is ViewModelAction.StartRefreshing -> getCurrentRates(action.currency)
            is ViewModelAction.CoefficientChanges -> setNewCoefficient(action.newValue)
        }
    }

    private fun setNewCoefficient(newValue: Float) {
        coefficientTransformation = newValue
    }

    private fun getCurrentRates(currency: String) {
        state.postValue(ViewModelState.Loading)
        disposable.dispose()
        disposable = repository.getValuesFor(currency)
            .repeat()
            .sample(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response ->
                if (response == null) {
                    throw Throwable("getCurrentRates: got null")
                }
                calculator.calculate(response.rates, coefficientTransformation)
            }
            .subscribeWith(object : DisposableObserver<List<Presentations>>() {
                override fun onComplete() {
                    disposable.dispose()
                }

                override fun onNext(updatedRates: List<Presentations>) {
                    state.postValue(ViewModelState.Result(updatedRates))
                }

                override fun onError(throwable: Throwable) {
                    state.postValue(ViewModelState.Error(throwable.message!!))
                }

            })
    }

}

sealed class ViewModelAction {
    class StartRefreshing(val currency: String) : ViewModelAction()
    class CoefficientChanges(val newValue: Float) : ViewModelAction()
}

sealed class ViewModelState {
    object Loading : ViewModelState()
    class Error(val message: String) : ViewModelState()
    class Result(val presentations: List<Presentations>) : ViewModelState()
}