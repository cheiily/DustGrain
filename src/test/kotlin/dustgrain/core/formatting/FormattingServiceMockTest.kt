package dustgrain.core.formatting

import dustgrain.core.ApiMockTest
import dustgrain.core.domain.DataField
import dustgrain.core.domain.DataHeader
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual

class FormattingServiceMockTest : ApiMockTest({
    val someSingleDataField = DataField(
        name = "someDataField",
        type = "someType",
        content = "someContent"
    )
    val someListDataField = DataField(
        name = "someListDataField",
        type = "someType",
        content = "someContent1;someContent2;someContent3"
    )

    fun FormatterRef.toSomeDataHeader(delimiter: String? = null) = DataHeader(
        name = "someDataField",
        type = this,
        delimiter = delimiter
    )

    val allDataHeaders = FormatterRef.entries.map(FormatterRef::toSomeDataHeader)

    feature("FormattingService#matchFormat") {
        scenario("matches reference to formatter") {
            FormatterRef.IMAGE.toSomeDataHeader().let {
                mockFormattingService.matchFormat(someSingleDataField, it) shouldBeEqual mockFormattingService.formatImage
            }
            FormatterRef.PASS.toSomeDataHeader().let {
                mockFormattingService.matchFormat(someSingleDataField, it) shouldBeEqual mockFormattingService.formatPass
            }
            FormatterRef.PASS_ERROR.toSomeDataHeader().let {
                mockFormattingService.matchFormat(someSingleDataField, it) shouldBeEqual mockFormattingService.formatErrorPass
            }
            FormatterRef.WIKITEXT.toSomeDataHeader().let {
                mockFormattingService.matchFormat(someSingleDataField, it) shouldBeEqual mockFormattingService.formatWikitext
            }
        }
    }

    feature("FormattingService#formatPass") {
        scenario("parses single item content") {
            // given
            val data = someSingleDataField to FormatterRef.PASS.toSomeDataHeader()

            // when
            val result = mockFormattingService.formatPass.format(data)

            // then
            result.name shouldBeEqual someSingleDataField.name
            result.contents shouldBeEqual listOf(someSingleDataField.content)
        }

        scenario("parses list content") {
            // given
            val data = someListDataField to FormatterRef.PASS.toSomeDataHeader(delimiter = ";")

            // when
            val result = mockFormattingService.formatPass.format(data)

            // then
            result.name shouldBeEqual someListDataField.name
            result.contents shouldBeEqual listOf("someContent1", "someContent2", "someContent3")
        }
    }

    feature("FormattingService#formatErrorPass") {
        scenario("parses single item content") {
            // given
            val data = someSingleDataField to FormatterRef.PASS_ERROR.toSomeDataHeader()

            // when
            val result = mockFormattingService.formatErrorPass.format(data)

            // then
            result.name shouldBeEqual someSingleDataField.name
            result.contents shouldBeEqual listOf(someSingleDataField.content)
        }

        scenario("parses list content as single item") {
            // given
            val data = someListDataField to FormatterRef.PASS_ERROR.toSomeDataHeader(delimiter = ";")

            // when
            val result = mockFormattingService.formatErrorPass.format(data)

            // then
            result.name shouldBeEqual someListDataField.name
            result.contents shouldBeEqual listOf(someListDataField.content)
        }
    }

    feature("FormattingService#formatImage") {
        scenario("parses single item content") {
            // given
            thereIsImageData("1")
            val data = someSingleDataField to FormatterRef.IMAGE.toSomeDataHeader()

            // when
            val result = mockFormattingService.formatImage.format(data)

            // then
            result.name shouldBeEqual someSingleDataField.name
            result.contents shouldBeEqual listOf("https://www.dustloop.com/wiki/images/e/e8/BBCF_Noel_Vermillion_d623D.png")
        }

        scenario("parses list content") {
            // given
            val data = someListDataField to FormatterRef.IMAGE.toSomeDataHeader(delimiter = ";")

            // when
            val result = mockFormattingService.formatImage.format(data)

            // then
            result.name shouldBeEqual someListDataField.name
            result.contents shouldBeEqual listOf(
                "https://www.dustloop.com/wiki/images/e/e8/BBCF_Noel_Vermillion_d623D.png",
                "https://www.dustloop.com/wiki/images/e/e8/BBCF_Noel_Vermillion_d623D.png",
                "https://www.dustloop.com/wiki/images/e/e8/BBCF_Noel_Vermillion_d623D.png"
            )
        }
    }

    feature("FormattingService#formatWikitext") {
        scenario("is not implemented yet") {
            // given
            val data = someSingleDataField to FormatterRef.WIKITEXT.toSomeDataHeader()

            // then
            shouldThrow<NotImplementedError> {
                mockFormattingService.formatWikitext.format(data)
            }
        }
    }

    feature("FormattingService#format") {
        scenario("formats data according to header type") {
            // given
            thereIsImageData("1")
            val data = listOf(
                someSingleDataField to FormatterRef.PASS.toSomeDataHeader(),
                someSingleDataField to FormatterRef.PASS_ERROR.toSomeDataHeader(),
                someSingleDataField to FormatterRef.IMAGE.toSomeDataHeader(),
                // todo #14
//                someSingleDataField to FormatterRef.WIKITEXT.toSomeDataHeader()
            )

            // when
            val result = mockFormattingService.format(data)

            // then
            result.size shouldBeEqual 3
            result[0].contents shouldBeEqual listOf(someSingleDataField.content)
            result[1].contents shouldBeEqual listOf(someSingleDataField.content)
            result[2].contents shouldBeEqual listOf("https://www.dustloop.com/wiki/images/e/e8/BBCF_Noel_Vermillion_d623D.png")

        }
    }
})