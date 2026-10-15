package com.example.tugas5

import androidx.annotation.DimenRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
private fun dimenSp(@DimenRes id: Int): TextUnit = dimensionResource(id).value.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasScreen() {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var skillAndroid by remember { mutableStateOf(false) }
    var skillUiux by remember { mutableStateOf(false) }
    var skillWeb by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var expandExp by remember { mutableStateOf(false) }

    val expOptions = stringArrayResource(R.array.experience_options).toList()
    val savedMessage = stringResource(R.string.snackbar_message)
    val datePattern = stringResource(R.string.date_format)
    val snackbarDurationMs = integerResource(R.integer.snackbar_duration_ms).toLong()
    val maxNameLength = integerResource(R.integer.max_name_length)
    val maxEmailLength = integerResource(R.integer.max_email_length)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val slides = listOf(
        R.drawable.slide_1,
        R.drawable.slide_2,
        R.drawable.slide_3,
        R.drawable.slide_4
    )
    val pagerState = rememberPagerState(pageCount = { slides.size })

    val isFormValid = firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            email.isNotBlank() &&
            birthDate.isNotBlank() &&
            experience.isNotBlank() &&
            (skillAndroid || skillUiux || skillWeb)

    val purpleButton = colorResource(R.color.purple_button)
    val fieldShape = RoundedCornerShape(dimensionResource(R.dimen.field_corner))
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = purpleButton,
        unfocusedBorderColor = colorResource(R.color.gray_border),
        disabledBorderColor = colorResource(R.color.gray_border),
        disabledTextColor = colorResource(R.color.black),
        disabledLabelColor = colorResource(R.color.gray_text),
        disabledTrailingIconColor = colorResource(R.color.black),
        focusedLabelColor = purpleButton,
        unfocusedLabelColor = colorResource(R.color.dark_gray)
    )

    if (showDatePicker) {
        val dateState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat(datePattern, Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        birthDate = sdf.format(Date(millis))
                    }
                    showDatePicker = false
                }) { Text(stringResource(R.string.date_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.date_cancel))
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colorResource(R.color.background_screen)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Layer 1: gambar slide (4 foto, swipe)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.image_height))
            ) { page ->
                Image(
                    painter = painterResource(id = slides[page]),
                    contentDescription = stringResource(R.string.cd_slide_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Indikator titik
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(dimensionResource(R.dimen.dot_padding)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dot_spacing))
            ) {
                repeat(slides.size) { index ->
                    val dotColor = if (pagerState.currentPage == index) {
                        colorResource(R.color.dot_active)
                    } else {
                        colorResource(R.color.dot_inactive)
                    }
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.dot_size))
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                }
            }

            // Layer 2: ElevatedCard mengapung menimpa gambar.
            // Tinggi 75% layar -> otomatis sisa 1/4 di bawah (dari fillMaxHeight).
            ElevatedCard(
                shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner)),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = dimensionResource(R.dimen.card_elevation)
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = colorResource(R.color.white)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.90f)
                    .padding(
                        start = dimensionResource(R.dimen.card_horizontal_margin),
                        end = dimensionResource(R.dimen.card_horizontal_margin),
                        top = dimensionResource(R.dimen.card_top_offset)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = dimensionResource(R.dimen.card_padding_h),
                            vertical = dimensionResource(R.dimen.card_padding_v)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.form_title),
                        color = colorResource(R.color.purple_title),
                        fontSize = dimenSp(R.dimen.title_size),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_s)))
                    Text(
                        text = stringResource(R.string.form_subtitle),
                        color = colorResource(R.color.gray_text),
                        fontSize = dimenSp(R.dimen.subtitle_size),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_m)))

                    HorizontalDivider(
                        thickness = dimensionResource(R.dimen.divider_thickness),
                        color = colorResource(R.color.divider_color)
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_m)))

                    // Nama depan & belakang: presisi sama (fillMaxWidth + weight 1:1)
                    Row(
                        modifier = Modifier.fillMaxWidth(),   // <- KUNCI presisi
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_s))
                    ) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { if (it.length <= maxNameLength) firstName = it },
                            label = { Text(stringResource(R.string.hint_first_name)) },
                            singleLine = true,
                            shape = fieldShape,
                            colors = fieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { if (it.length <= maxNameLength) lastName = it },
                            label = { Text(stringResource(R.string.hint_last_name)) },
                            singleLine = true,
                            shape = fieldShape,
                            colors = fieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_m)))

                    // Email: satu-satunya yang full
                    OutlinedTextField(
                        value = email,
                        onValueChange = { if (it.length <= maxEmailLength) email = it },
                        label = { Text(stringResource(R.string.hint_email)) },
                        singleLine = true,
                        shape = fieldShape,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_m)))

                    // Tgl Lahir & Experience: presisi sama (fillMaxWidth + weight 1:1)
                    Row(
                        modifier = Modifier.fillMaxWidth(),   // <- KUNCI presisi
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_s))
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = birthDate,
                                onValueChange = {},
                                readOnly = true,
                                enabled = false,
                                label = { Text(stringResource(R.string.hint_birth_date)) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.DateRange,
                                        contentDescription = stringResource(R.string.cd_pick_date)
                                    )
                                },
                                singleLine = true,
                                shape = fieldShape,
                                colors = fieldColors,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { showDatePicker = true }
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = experience,
                                onValueChange = {},
                                readOnly = true,
                                enabled = false,
                                label = { Text(stringResource(R.string.hint_experience)) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = stringResource(R.string.cd_pick_experience)
                                    )
                                },
                                singleLine = true,
                                shape = fieldShape,
                                colors = fieldColors,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { expandExp = true }
                            )
