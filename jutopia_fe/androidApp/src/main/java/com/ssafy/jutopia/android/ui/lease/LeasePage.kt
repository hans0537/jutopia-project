package com.ssafy.jutopia.android.ui.lease

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.jutopia.android.model.Seat

@Composable
fun LeasePage(viewModel: LeaseViewModel = viewModel(), modifier: Modifier = Modifier) {
    val seats by viewModel.seats.collectAsState()
    val selectedSeat by viewModel.selectedSeat.collectAsState()

    Box(
        modifier = Modifier.fillMaxWidth()
            .background(Color(0xFFEDEDED))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(50.dp)
        ) {
            Text("칠판", color = Color.White, modifier = Modifier.align(Alignment.Center))
        }
        Spacer(modifier = Modifier.height(100.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
        )
        {//한 줄에 배치될 좌석
            items(seats.chunked(5)) { rowSeats ->
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowSeats.forEach { seat ->
                        SeatView(seat, viewModel)
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        }

        if (selectedSeat != null) {
            val isAvailable = selectedSeat!!.isAvailable
            AlertDialog(
                onDismissRequest = { viewModel.clearSelectedSeat() },
                shape = RoundedCornerShape(20.dp), // 둥근 모서리 설정
                // 그림자 설정은 여기서 직접적으로 할 수 없음. 따라서, 박스를 추가할 수 있다.
                title = {
                    Text("자리 정보", textAlign = TextAlign.Center)
                },
                text = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(20.dp)), // 그림자 설정
                        contentAlignment = Alignment.Center // 가운데 정렬
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally, // 가운데 정렬
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("자리: ${selectedSeat?.id ?: ""}")
                            Text("가격: ${selectedSeat?.info ?: ""}")
                            Text("구매하시겠습니까?")
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (isAvailable) {
                                viewModel.reserveSeat(selectedSeat!!.id)
                                viewModel.clearSelectedSeat()
                            }
                        },
                        enabled = isAvailable,
                        modifier = Modifier.align(Alignment.Center) // 가운데 정렬
                    ) {
                        Text("신청")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            viewModel.clearSelectedSeat()
                        },
                        modifier = Modifier.align(Alignment.Center) // 가운데 정렬
                    ) {
                        Text("취소")
                    }
                }
            )
        }
    }


}

@Composable
fun SeatView(seat: Seat, viewModel: LeaseViewModel) {
    val backgroundColor = if (seat.isAvailable) Color.Green else Color.Gray
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(50.dp) 
            .background(backgroundColor)
            .clickable { viewModel.selectSeat(seat) }
    ) {
        Text(
            text = "${seat.id}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}