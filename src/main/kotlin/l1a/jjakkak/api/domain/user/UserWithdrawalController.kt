package l1a.jjakkak.api.domain.user

import l1a.jjakkak.api.ApiUrl
import l1a.jjakkak.api.domain.user.reqeust.UserWithdrawalRequest
import l1a.jjakkak.api.domain.user.response.WithdrawalResponse
import l1a.jjakkak.core.domain.user.UserId
import l1a.jjakkak.core.domain.user.usecase.WithdrawalUseCase
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
internal interface UserWithdrawalController {
    @PostMapping(ApiUrl.USER_WITHDRAWAL)
    fun userWithdrawal(@RequestHeader headers: HttpHeaders): WithdrawalResponse



}
@RestController
internal class UserWithdrawalControllerImpl (
    val useCase: WithdrawalUseCase
): UserWithdrawalController  {

    override fun userWithdrawal(h: HttpHeaders): WithdrawalResponse {
        var token = h["Authorization"].toString().replace("Bearer ", "")
        return  useCase.withdrawal(UserWithdrawalRequest(token).toMessage()).run { WithdrawalResponse.from(this) }
    }
}