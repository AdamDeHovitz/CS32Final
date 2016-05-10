bulkAppControllers.controller("writeReviewCtrl", 
		function($scope, $rootScope, $http, $stateParams, $ionicModal, $state) {
	
	$scope.forms = {};
	$scope.reviewCreate = {rating: 3};
	
	$.post("/pending-reviews", {id: $rootScope.account.id}, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		console.log(responseObject);
		$scope.reviews = responseObject.pendingReviews;
	});
	
	
	$scope.reviewUser = function(review) {
		$scope.curReview = review;
		$scope.openModal();
		console.log("reviewing!");
	};
	
	$scope.deleteReview = function(ReviewId) {
		console.log("deleting!")
	};
	
	$scope.goProfile = function(id) {
		$state.go('tab.events-profile',{userId: id});
	};
	
	$scope.createReview = function(targetId, pendingId) {
		$.post("/review", {authorId: $rootScope.account.id, targetId: targetId, 
			text: $scope.reviewCreate.text, rating: $scope.reviewCreate.rating, pendingId: pendingId},
			function(responseJSON) {
				
			});
		var newReviews = [];
		for (var i = 0; i < $scope.reviews.length; i++) {
			if ($scope.reviews[i].id != pendingId) {
				newReviews.push($scope.reviews[i]);
			}
		}
		$scope.reviews = newReviews;
		console.log($scope.reviewCreate);
		$scope.modal.hide();
	}
	
	$ionicModal.fromTemplateUrl('new-review-modal.html', {
    scope : $scope,
    animation : 'slide-in-up'
  }).then(function(modal) {
    $scope.modal = modal;
  });
  $scope.openModal = function() {
    $scope.modal.show();
  };
  $scope.closeModal = function() {
    $scope.modal.hide();
    $scope.reviewCreate = {rating: 3};
  };
  // Cleanup the modal when we're done with it!
  $scope.$on('$destroy', function() {
    $scope.modal.remove();
  });
  // Execute action on hide modal
  $scope.$on('modal.hidden', function() {
    // Execute action
  });
  // Execute action on remove modal
  $scope.$on('modal.removed', function() {
    // Execute action
  });
	
});