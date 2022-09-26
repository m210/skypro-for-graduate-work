package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.skypro.homework.models.dto.AdsDto;
import ru.skypro.homework.models.dto.CreateAdsDto;
import ru.skypro.homework.models.dto.FullAdsDto;
import ru.skypro.homework.models.entity.Ads;
import ru.skypro.homework.models.entity.Images;
import ru.skypro.homework.models.entity.User;
import ru.skypro.homework.models.mappers.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final UserService userService;
    private final AdsRepository adsRepository;
    private final AdsMapper adsMapper;

    @Override
    public List<AdsDto> getALLAds() {
        log.info("Trying to get all ads");
        List<Ads> allAds = adsRepository.findAll();
        if (allAds.isEmpty()) {
            log.warn("Ads not found");
            throw new NotFoundException("Ads not found");
        }

        return toAdsDtoList(allAds);
    }

    @Override
    public AdsDto addAds(CreateAdsDto ads, Images images) {
        log.info("Trying to add new ad");
        User user = userService.getUser(0);//todo заменить на текущего авторизованного юзера
        Ads newAds = adsMapper.fromCreateAds(ads, user, images);
        Ads response = adsRepository.save(newAds);
        log.info("The ad with pk = {} was saved ", response.getPk());

        return adsMapper.toAdsDto(response);
    }

    @Override
    public List<AdsDto> getAdsMe(Boolean authenticated, String authority, Object credentials, Object details, Object principal) {
        // FIXME: Just returns all
        log.info("Trying to get all user's ads");

        return toAdsDtoList(adsRepository.findAll());
    }

    @Transactional
    @Override
    public void removeAds(Integer id) {
        log.info("Trying to remove the ad with id = {}", id);
        getAds(id);
        adsRepository.deleteById(id);
        log.info("The ad with id = {} was removed", id);
    }

    @Override
    public FullAdsDto getFullAds(Integer id) {
        Ads ads = getAds(id);

        return adsMapper.toFullAdsDto(ads);
    }

    @Transactional
    @Override
    public AdsDto updateAds(Integer id, CreateAdsDto adsDto) {
        log.info("Trying to update the ad with id = {}", id);
        Ads ads = getAds(id);
        User user = userService.getUser(0);//todo использовать текущего юзера
        Ads updatedAds = adsMapper.fromCreateAds(adsDto, user, ads.getImage());
        updatedAds.setComments(List.copyOf(ads.getComments()));
        updatedAds.setPk(id);
        Ads saveAds = adsRepository.save(updatedAds);
        log.info("The ad with id = {} was updated ", id);

        return adsMapper.toAdsDto(saveAds);
    }

    @Override
    public Ads getAds(Integer ad_pk) {
        log.info("Trying to get the ad with id = {}", ad_pk);
        Ads ads = adsRepository.findById(ad_pk)
                .orElseThrow(() -> {
                    log.warn("The ad with id = {} does not exist", ad_pk);
                    return new NotFoundException("Ad with id = " + ad_pk + " does not exist");
                });
        log.info("The ad with id = {} was found", ad_pk);

        return ads;
    }


    /**
     * @param title - the parameter to search a title like...
     * @return list of found ads
     */
    @Override
    public List<AdsDto> findAds(String title) {
        log.info("Trying to find ads like {}", title);
        return toAdsDtoList(adsRepository.findByTitleLikeIgnoreCase("%" + title + "%"));
    }

    /**
     * @param order a sorting direction (ordering by increasing or decreasing)
     * @return ordered list of all ads
     */
    @Override
    public List<AdsDto> getSortedAds(Sort.Direction order) {
        return toAdsDtoList(adsRepository.findAll(Sort.by(order, "title")));
    }

    private List<AdsDto> toAdsDtoList(List<Ads> ads) {
        return ads.stream().map(adsMapper::toAdsDto).collect(Collectors.toList());
    }
}
