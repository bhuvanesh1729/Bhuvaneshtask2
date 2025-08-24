package com.bhuvanesh.task.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhuvanesh.task.data.uidata.BottomNavigationItem
import com.bhuvanesh.task.data.uidata.UIData
import com.bhuvanesh.task.data.uidata.UIState
import com.bhuvanesh.task.domain.usecases.IUserPortfolio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoldingsViewModel @Inject constructor(val userPortfolio: IUserPortfolio) : ViewModel() {

    private val _uiData = MutableStateFlow<UIState<UIData>>(UIState.Loading)
    val uiData = _uiData.asStateFlow()
    private val _bottomBarItems = MutableStateFlow<List<BottomNavigationItem>>(BottomNavigationItem.tabs)
    val bottomBarItems = _bottomBarItems.asStateFlow()
    init {
        initialiseData()
    }

    fun initialiseData() {
        viewModelScope.launch {
            userPortfolio.invoke().collectLatest {
                _uiData.value = it
            }
        }
    }
}